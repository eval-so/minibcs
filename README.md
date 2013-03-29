# minibcs

The Eval.gd Compilation System.

# Hacking

We use SBT extensively, and so we recommend having a fairly up to date version
of it.

This is the core of Eval.gd, so it's reasonable to expect that anyone who
hacks on this a lot has a setup somewhat-similar to the production setup. That
is, they have SELinux enforcing, they have the SELinux `sandbox` command, and
they have interpreters for languages they'll be modifying/adding set up, for
testing purposes.

## Building and Deploying ScalaDoc

We use the cool [sbt-ghpages](https://github.com/sbt/sbt-ghpages) plugin, which
internally uses [sbt-site](https://github.com/sbt/sbt-site).

To deploy the docs, ensure you have push access to the repository, then run
`sbt ghpages-push-site`.

# License

Apache 2.0.
