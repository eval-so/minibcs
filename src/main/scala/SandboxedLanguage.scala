package gd.eval

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.sys.process._

import java.io.{BufferedWriter, File, FileWriter}

trait SandboxedLanguage {
  private val stdout = new StringBuilder
  private val stderr = new StringBuilder

  private val logger = ProcessLogger(
    out => stdout.append(out),
    err => stderr.append(err))

  /** Other binaries that should be on the system to use this language. */
  val extraRequiredBinaries: Option[List[String]] = None

  /** Extension of the language we're evaluating, without period. */
  val extension: String

  /** Source code of the program being evaluated. */
  val code: String

  /** After how long, in seconds, should we kill the evaluation? */
  val timeout: Int = 5

  /** What file should we store the code in?
    *
    * This is lazy because extension doesn't exist yet.
    */
  protected lazy val file: File = new File(s"/tmp/home/${System.currentTimeMillis}/${System.currentTimeMillis}.${extension}")

  /** How do we run the code? */
  val command: Seq[String]

  /** How do we sandbox the eval? */
  private val sandboxCommand: Seq[String] = Seq(
    "timeout", timeout.toString, "sandbox", "-H", "/tmp/home", "-T",
    "/tmp/home/.tmp", "-t", "sandbox_x_t", "timeout", timeout.toString)

  private def writeCodeToFile() {
    file.getParentFile.mkdirs()
    val output = new BufferedWriter(new FileWriter(file))
    output.write(code)
    output.flush
  }

  /** Return a Boolean indicating whether or not SELinux is enforcing. */
  private def isSELinuxEnforcing() = "getenforce".!!.trim == "Enforcing"

  /** The result of an evaluation. */
  case class Result(
    stdout: String,
    stderr: String,
    wallTime: Long,
    exitCode: Int
  )

  /** Evaluate the code.
    *
    * @return a Left[Throwable] if we hit an internal issue, such as SELinux being altered.
    *         Otherwise, a Right[Future[SandboxedLanguage]].
    */
  def evaluate() =
    if (isSELinuxEnforcing()) {
      writeCodeToFile()
      val startTime = System.currentTimeMillis
      val exitCode = (sandboxCommand ++ command) ! logger
      val wallTime = System.currentTimeMillis - startTime

      val result = Result(
        stdout.toString,
        stderr.toString,
        wallTime,
        exitCode)
      Right(result)
    } else {
      Left(new SecurityException("SELinux is not enforcing. Bailing out early."))
    }
}
