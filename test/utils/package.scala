import io.Source
import xml.{XML, Elem}

/**
 * Copyright (c) Cisco systems 2013. All rights reserved.
 * Date: 24/03/2013
 */
package object utils {
  def getXmlReader(fileName: String): Elem = {
    XML.load(Source.fromFile(fileName).reader())
  }
}
