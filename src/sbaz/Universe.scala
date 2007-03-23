/* SBaz -- Scala Bazaar
 * Copyright 2005-2007 LAMP/EPFL
 * @author  Lex Spoon
 */

// $Id$

package sbaz

import scala.xml._
import java.io.File

/** A universe is a visible set of available packages that
  * can change over time.
  */
abstract class Universe(val name: String, val description: String) {
//XXX remove the name and description from here.  The description
//should die and the name should only be in simple universe.
  def toXML: Node

  def retrieveAvailable(): AvailableList

  def simpleUniverses: List[SimpleUniverse]

  /** Inform this universe that it can save its keyring
    * files in the specified directory.  This is only
    * meaningful for client programs.
    */
  def keyringFilesAreIn(dir: File): Unit = ()
}


object Universe {
  def fromXML(node: Node): Universe = {
    node match {
      case node: Elem =>
        val name = node.label
        name match {
          case "overrideuniverse" =>
            OverrideUniverse.fromXML(node)

          case "simpleuniverse" =>
            SimpleUniverseUtil.fromXML(node)

          case "emptyuniverse" =>
            new EmptyUniverse()

          case _ =>
            throw new XMLFormatError(node)
        }

      case _ =>
        throw new XMLFormatError(node)
    }
  }
}
