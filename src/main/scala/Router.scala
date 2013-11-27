package so.eval
import so.eval.languages._

import akka.actor.Actor

object Router {
  /* The list of languages we can evaluate. Keep alphabetical. */
  val languages = Map(
    "bash" -> Bash,
    "c" -> C,
    "c++" -> `C++`,
    "chickenscheme" -> ChickenScheme,
    "clojure" -> Clojure,
    "csharp" -> CSharp,
    "elixir" -> Elixir,
    "factor" -> Factor,
    "fsharp" -> FSharp,
    "go" -> Go,
    "haskell" -> Haskell,
    "idris" -> Idris,
    "io" -> Io,
    "java" -> Java,
    "jruby18" -> JRuby18,
    "jruby19" -> JRuby19,
    "lolcode" -> LOLCODE,
    "lua" -> Lua,
    "perl" -> Perl,
    "perl6" -> Perl6,
    "php" -> PHP,
    "python2" -> Python2,
    "python3" -> Python3,
    "ruby" -> Ruby,
    "rust" -> Rust,
    "scala" -> Scala,
    "smalltalk" -> Smalltalk,
    "sml-mlton" -> SML,
    "vbnet" -> VBNET
  )

  /** Hack to give certain languages a different display name.
    *
    * I fought for hours trying to work out a better way to do this, but for now
    * this will band-aid around the issue.
    *
    * We basically want displayName to be a static field on an implementation of
    * [[so.eval.SandboxedLanguage]]...but only some instances of it, which is
    * our first issue. Our second issue is that even if we defined a companion
    * object on all implementations, our language map above would break as it
    * infers [String, Object] instead of the convoluted AbstractFunction1
    * conjunction it comes up with now.
    *
    * Simply put, I've tried a bunch of other options, and couldn't come up with
    * any simple way to solve this, but we should try to at some point.
    */
  val languageDisplayName = Map(
    "chickenscheme" -> "Chicken Scheme",
    "csharp" -> "C#",
    "fsharp" -> "F#",
    "python2" -> "Python 2",
    "python3" -> "Python 3",
    "jruby18" -> "JRuby (1.8 mode)",
    "jruby19" -> "JRuby (1.9 mode)",
    "lolcode" -> "LOLCODE (via lci)",
    "perl6" -> "Perl 6 (Rakudo Star)",
    "php" -> "PHP",
    "rust" -> "Rust 0.7",
    "sml-mlton" -> "SML (MLton)",
    "vbnet" -> "VB.NET"
  )

  /** Return the proper display name for a language. */
  def displayName(key: String) = languages.get(key) match {
    case Some(language) => Some(languageDisplayName.get(key).getOrElse(key.capitalize))
    case _ => None
  }

  /** LEGACY: Return a subclass of [[SandboxedLanguage]], configured to run an eval. */
  def route(language: String, code: String) = languages.get(language).map(_(EvaluationRequest(code)))

  /** Return a subclass of [[SandboxedLanguage]], configured to run an eval via Akka.
    *
    * Using this method, you can get an instance of a class that extends
    * [[SandboxedLanguage]]. This is useful to prepare a message to send to the
    * [[Router]] actor defined below.
    *
    * All new clients implementing BCS should use this method as opposed to
    * route[String, String](), even if they just call `.evaluate()` on the
    * resulting class.
    */
  def route(language: String, evaluation: EvaluationRequest) = languages.get(language).map(_(evaluation))
}

class Router extends Actor {
  def receive = {
    case s: SandboxedLanguage => sender ! s.evaluate
  }
}
