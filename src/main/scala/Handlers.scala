// (C) Uri Wilensky. https://github.com/NetLogo/Tortoise

package org.nlogo.tortoise

import org.nlogo.{ api, compile => ast, nvm }

trait Handlers {

  def prims: Prims

  def fun(node: ast.AstNode, isReporter: Boolean = false, isTask: Boolean = false): String = {
    val taskHeader =
      if (isTask)
        "var taskArguments = arguments;\n"
      else
        ""
    val body = taskHeader +
      (if (isReporter)
         "return " + reporter(node) + ";"
       else
         commands(node))
    def isTrivialReporter(node: ast.AstNode): Boolean =
      node match {
        case block: ast.ReporterBlock =>
          isTrivialReporter(block.app)
        case app: ast.ReporterApp =>
          app.args.isEmpty && app.reporter.isInstanceOf[nvm.Pure]
        case _ =>
          false
      }
    if (body.isEmpty)
      "function() {}"
    else if (isTrivialReporter(node))
      s"function() { $body }"
    else
      s"""|function() {
          |${indented(body)}
          |}""".stripMargin
  }

  // The "abstract" syntax trees we get from the front end aren't totally abstract in that they have
  // CommandBlock wrappers around Statements objects and ReporterBlock wrappers around Reporter
  // objects, representing the concrete syntax of square brackets, but at this stage of compilation
  // the brackets are irrelevant.  So when we see a block we just immediately recurse into it.

  def commands(node: ast.AstNode): String =
    node match {
      case block: ast.CommandBlock =>
        commands(block.statements)
      case statements: ast.Statements =>
        statements.stmts.map(prims.generateCommand)
          .filter(_.nonEmpty)
          .mkString("\n")
    }

  def reporter(node: ast.AstNode): String = node match {
    case block: ast.ReporterBlock =>
      reporter(block.app)
    case app: ast.ReporterApp =>
    prims.reporter(app)
  }

  def literal(obj: AnyRef): String = obj match {
    case ll: api.LogoList =>
      ll.map(literal).mkString("[", ", ", "]")
    case x =>
      api.Dump.logoObject(x, readable = true, exporting = false)
  }

  def indented(s: String): String =
    s.lines.map("  " + _).mkString("\n")

  // bogus, will need work - ST 9/13/13
  def ident(s: String): String = {
    def initialUpper(s: String): String =
      java.lang.Character.toUpperCase(s.head) + s.tail
    def initialLower(s: String): String =
      java.lang.Character.toLowerCase(s.head) + s.tail
    val camel = initialLower(s.toLowerCase.split('-').map(initialUpper).mkString)
    camel
      .replaceAll("\\?", "_p")
      .replaceAll("%", "_percent_")
  }

}
