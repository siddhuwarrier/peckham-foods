package models.factory

import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers
import scala.io.Source
import xml.{Elem, XML}
import play.api.test.FakeApplication
import play.api.test.Helpers.running
import models.Product

class ProductFactorySpec extends FunSpec with ShouldMatchers {
  describe("The Product Factory") {
    it("should build a product with the details extracted from an XML entity") {
      running(FakeApplication()) {
        val xmlElem = ProductFactorySpec.getXmlReader(ProductFactorySpec.VALID_SAMPLE_PRODUCT_FILE)
        val productOption = ProductFactory.build(xmlElem)
        productOption should not be (None)

        val product = productOption.get
        product.productId should equal(xmlElem.attributes.value(0).text)
        product.productName should equal((xmlElem \ ProductFactory.PRODUCT_NAME_XML_ELEMENT).text)
        product.ean should equal((xmlElem \ ProductFactory.PRODUCT_EAN_XML_ELEMENT).text)
        product.listPrice should equal((xmlElem \ ProductFactory.PRODUCT_LIST_PRICE_XML_ELEMENT).text.toDouble)
        product.wholesalePrice should equal((xmlElem \ ProductFactory.PRODUCT_WHOLESALE_PRICE_XML_ELEMENT).text.toDouble)
        product.currency.getCurrencyCode should equal((xmlElem \ ProductFactory.PRODUCT_LIST_PRICE_XML_ELEMENT \ ProductFactory.PRODUCT_CURRENCY_XML_ATTRIB).text)
      }

    }

    it("should not build a product if the XML entity is empty") {
      val xmlElem = ProductFactorySpec.getXmlReader(ProductFactorySpec.EMPTY_SAMPLE_PRODUCT_FILE)
      ProductFactory.build(xmlElem) should be(None)
    }

    it("should not build a product if the name is missing") {
      val xmlElem = ProductFactorySpec.getXmlReader(ProductFactorySpec.SAMPLE_PRODUCT_FILE_WITH_NO_NAME)
      ProductFactory.build(xmlElem) should be(None)
    }

    it("should not build a product if the EAN is missing") {
      val xmlElem = ProductFactorySpec.getXmlReader(ProductFactorySpec.SAMPLE_PRODUCT_FILE_WITH_NO_EAN)
      ProductFactory.build(xmlElem) should be(None)
    }

    it("should not build a product if the list price is missing") {
      val xmlElem = ProductFactorySpec.getXmlReader(ProductFactorySpec.SAMPLE_PRODUCT_FILE_WITH_NO_LIST_PRICE)
      ProductFactory.build(xmlElem) should be(None)

    }

    it("should not build a product if the wholesale price is missing") {
      val xmlElem = ProductFactorySpec.getXmlReader(ProductFactorySpec.SAMPLE_PRODUCT_FILE_WITH_NO_WHOLESALE_PRICE)
      ProductFactory.build(xmlElem) should be(None)

    }

    it("should not build a product if list price is not a double") {
      val xmlElem = ProductFactorySpec.getXmlReader(ProductFactorySpec.SAMPLE_PRODUCT_FILE_WITH_NON_NUMERIC_LIST_PRICE)
      ProductFactory.build(xmlElem) should be(None)
    }

    it("should not build a product if the list price and wholesale price have different currencies") {
      val xmlElem = ProductFactorySpec.getXmlReader(ProductFactorySpec.SAMPLE_PRODUCT_FILE_WITH_DIFFERENT_CURRENCY_ATTRIBUTES)
      ProductFactory.build(xmlElem) should be(None)
    }

    it("should build a product with the default currency if the list price and wholesale price are missing a currency attribute") {
      running(FakeApplication()) {
        val xmlElem = ProductFactorySpec.getXmlReader(ProductFactorySpec.SAMPLE_PRODUCT_FILE_WITH_NO_CURRENCY_ATTRIBUTE)

        val productOption = ProductFactory.build(xmlElem)
        productOption should not be (None)

        val product = productOption.get
        product.productId should equal(xmlElem.attributes.value(0).text)
        product.productName should equal((xmlElem \ ProductFactory.PRODUCT_NAME_XML_ELEMENT).text)
        product.ean should equal((xmlElem \ ProductFactory.PRODUCT_EAN_XML_ELEMENT).text)
        product.listPrice should equal((xmlElem \ ProductFactory.PRODUCT_LIST_PRICE_XML_ELEMENT).text.toDouble)
        product.wholesalePrice should equal((xmlElem \ ProductFactory.PRODUCT_WHOLESALE_PRICE_XML_ELEMENT).text.toDouble)
        product.currency.getCurrencyCode should equal(Product.DEFAULT_CURRENCY_CODE)
      }
    }

    it("should not build a product if the currency attribute is invalid") {
      val xmlElem = ProductFactorySpec.getXmlReader(ProductFactorySpec.SAMPLE_PRODUCT_FILE_WITH_INVALID_CURRENCY_ATTRIBUTE)
      ProductFactory.build(xmlElem) should be(None)
    }

  }
}

object ProductFactorySpec {
  val VALID_SAMPLE_PRODUCT_FILE = "test/resources/product_xml/valid_sample_product.xml"
  val EMPTY_SAMPLE_PRODUCT_FILE = "test/resources/product_xml/empty_sample_product.xml"
  val SAMPLE_PRODUCT_FILE_WITH_NO_NAME = "test/resources/product_xml/sample_product_with_no_name.xml"
  val SAMPLE_PRODUCT_FILE_WITH_NO_EAN = "test/resources/product_xml/sample_product_with_no_ean.xml"
  val SAMPLE_PRODUCT_FILE_WITH_NO_LIST_PRICE = "test/resources/product_xml/sample_product_with_no_list_price.xml"
  val SAMPLE_PRODUCT_FILE_WITH_NO_WHOLESALE_PRICE = "test/resources/product_xml/sample_product_with_no_wholesale_price.xml"
  val SAMPLE_PRODUCT_FILE_WITH_NON_NUMERIC_LIST_PRICE = "test/resources/product_xml/sample_product_with_non_numeric_list_price.xml"
  val SAMPLE_PRODUCT_FILE_WITH_DIFFERENT_CURRENCY_ATTRIBUTES = "test/resources/product_xml/sample_product_with_different_currency_attributes.xml"
  val SAMPLE_PRODUCT_FILE_WITH_NO_CURRENCY_ATTRIBUTE = "test/resources/product_xml/sample_product_with_no_currency_attribute.xml"
  val SAMPLE_PRODUCT_FILE_WITH_INVALID_CURRENCY_ATTRIBUTE = "test/resources/product_xml/sample_product_with_invalid_currency_attributes.xml"

  def getXmlReader(fileName: String): Elem = {
    XML.load(Source.fromFile(fileName).reader())
  }
}
