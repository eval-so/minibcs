package so.eval

import org.apache.commons.codec.binary.Base64
import org.apache.commons.io.FileUtils

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.sys.process._
import scala.util.{Failure, Try, Success}

import java.io.{BufferedOutputStream, BufferedWriter, File, FileOutputStream, FileWriter}
import java.nio.file.Files

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
  val evaluation: EvaluationRequest

  /** After how long, in seconds, should we kill the evaluation? */
  val timeout: Int = 5

  /** A home directory that is unique to this evaluation. */
  val home = Files.createTempDirectory("eval-").toFile

  /** A /tmp mount for the sandbox (also available in its ~/.tmp). */
  val tmp = new File(s"${home}/.tmp")

  /** A place for evaluations to store things to ship back. */
  val outputFilesDir = new File(s"${home}/output")

  /** The code's filename. */
  lazy val filename = s"${home.getName}.${extension}"

  /** How do we compile the code, if we need to? */
  val compileCommand: Option[Seq[String]] = None

  /** How do we run the code? */
  val command: Seq[String]

  /** How do we sandbox the eval? */
  private val sandboxCommand: Seq[String] = Seq(
    "timeout", timeout.toString, "sandbox", "-H", home.toString, "-T",
    tmp.toString, "-t", "sandbox_x_t", "timeout", timeout.toString)

  /** Put the given code in its source file. */
  private def writeCodeToFile() {
    tmp.mkdirs()
    outputFilesDir.mkdirs()
    val output = new BufferedWriter(new FileWriter(new File(s"${home}/${filename}")))
    output.write(evaluation.code)
    output.close()
  }

  /** Return a Boolean indicating whether or not SELinux is enforcing. */
  private def isSELinuxEnforcing() = "getenforce".!!.trim == "Enforcing"

  /** Run a command in the Sandbox.
    *
    * @return A [[Result]] with the result.
    */
  private def runInSandbox(
    command: Seq[String],
    compilationResult: Option[SandboxedLanguage.Result] = None) = {
      val stdout = new StringBuilder
      val stderr = new StringBuilder
      val lineSeparator = sys.props.getOrElse("line.separator", "\n")
      val logger = ProcessLogger(
        out => stdout.append(out + lineSeparator),
        err => stderr.append(err + lineSeparator))
      val startTime = System.currentTimeMillis
      val exitCode = (sandboxCommand ++ command) ! logger
      val wallTime = System.currentTimeMillis - startTime

      def getOutputFiles(f: File = outputFilesDir): List[File] = {
        val these = Option(f.listFiles)
        these.map( f => (f ++ f.filter(_.isDirectory).flatMap(getOutputFiles)).filter(!_.isDirectory).toList ).getOrElse(List())
      }

      val base64OutputFiles = getOutputFiles().map { file =>
        file.getPath.replace(s"${home}/output/", "") -> Base64.encodeBase64String(io.Source.fromFile(file).map(_.toByte).toArray)
      }.toMap

      SandboxedLanguage.Result(
        stdout.toString,
        stderr.toString,
        wallTime,
        exitCode,
        compilationResult,
        if (base64OutputFiles.isEmpty) None else Some(base64OutputFiles))
  }


  /** Return a [[scala.util.Try[Result]]] after evaluating the code. */
  def evaluate() = {
    if (isSELinuxEnforcing()) {
      writeCodeToFile()

      evaluation.files match {
        case Some(files) => files.foreach { case (filename, base64Contents) =>
          val output = new BufferedOutputStream(new FileOutputStream(new File(s"${home}/${filename}")))
          output.write(Base64.decodeBase64(base64Contents))
          output.close()
        }
        case _ =>
      }

      val compilationResult = compileCommand match {
        case Some(command) => Some(runInSandbox(command))
        case _ => None
      }

      if (evaluation.compilationOnly && compilationResult != None) {
        FileUtils.deleteDirectory(home)

        // Acceptable since we check for None above.
        Try(compilationResult.get)
      } else {
        val result = runInSandbox(command, compilationResult)
        FileUtils.deleteDirectory(home)
        Try(result)
      }
    } else {
      Failure(new SecurityException("SELinux is not enforcing. Bailing out early."))
    }
  }
}

object SandboxedLanguage {
    /** The result of an evaluation. */
  case class Result(
    stdout: String,
    stderr: String,
    wallTime: Long,
    exitCode: Int,
    compilationResult: Option[Result] = None,
    outputFiles: Option[Map[String,String]] = None
  )
}
