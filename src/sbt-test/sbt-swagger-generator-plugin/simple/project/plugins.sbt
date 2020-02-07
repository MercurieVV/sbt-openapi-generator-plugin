resolvers += "jitpack" at "https://jitpack.io"
resolvers +=
  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

libraryDependencies += "com.github.mercurievv" % "openapi-codegen-http4s" % "1.0.7"
sys.props.get("plugin.version") match {
  case Some(x) => addSbtPlugin("com.github.mercurievv" % "sbt-openapi-generator-plugin" % x)
  case _ => sys.error("""|The system property 'plugin.version' is not defined.
                         |Specify this property using the scriptedLaunchOpts -D.""".stripMargin)
}