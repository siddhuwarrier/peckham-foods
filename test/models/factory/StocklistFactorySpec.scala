package models.factory

import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers

object StocklistFactorySpec {
  val VALID_SAMPLE_STOCKLIST_FILE = "test/resources/stocklist_xml/valid_sample_stocklist.xml"
  val EMPTY_SAMPLE_STOCKLIST_FILE = "test/resources/stocklist_xml/empty_sample_stocklist.xml"
  val INVALID_SAMPLE_STOCKLIST_FILE = "test/resources/stocklist_xml/invalid_sample_stocklist.xml"
  val MISSING_ELEMENT_IN_ONE_OF_THE_PRODUCTS_IN_INVALID_STOCKLIST = ProductFactory.PRODUCT_EAN_XML_ELEMENT
}

class StocklistFactorySpec extends FunSpec with ShouldMatchers {

  describe("The stock list factory") {
    it("should build a list of products from a stocklist XML equal to the number of products in the stocklist") {
      val xmlElem = utils.getXmlReader(StocklistFactorySpec.VALID_SAMPLE_STOCKLIST_FILE)
      val products = StocklistFactory.build(xmlElem)
      products.size should equal((xmlElem \ StocklistFactory.PRODUCT_XML_ELEMENT).length)
    }

    it("should build an empty list of products if the stocklist XML is empty") {
      val xmlElem = utils.getXmlReader(StocklistFactorySpec.EMPTY_SAMPLE_STOCKLIST_FILE)
      val products = StocklistFactory.build(xmlElem)
      products.size should equal(0)
    }

    it("should build a list of products from a stocklist XML that excludes all product entries that are malformed in the XML") {
      val xmlElem = utils.getXmlReader(StocklistFactorySpec.INVALID_SAMPLE_STOCKLIST_FILE)
      val products = StocklistFactory.build(xmlElem)
      products.size should equal((xmlElem
        \ StocklistFactory.PRODUCT_XML_ELEMENT
        \ StocklistFactorySpec.MISSING_ELEMENT_IN_ONE_OF_THE_PRODUCTS_IN_INVALID_STOCKLIST).length)
    }
  }

}
