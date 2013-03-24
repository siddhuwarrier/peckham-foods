package models.factory

import xml.{Node, MetaData}
import models.Product
import java.util.Currency

object ProductFactory {
  val PRODUCT_NAME_XML_ELEMENT = "name"
  val PRODUCT_EAN_XML_ELEMENT = "ean"
  val PRODUCT_LIST_PRICE_XML_ELEMENT = "listPrice"
  val PRODUCT_CURRENCY_XML_ATTRIB = "@currency"
  val PRODUCT_WHOLESALE_PRICE_XML_ELEMENT = "wholesalePrice"

  def build(productXml: Node): Option[Product] = {
    val getItem = getItemFromXml(productXml, _: String) //partially-applied functions FTW

    try {
      val product = new Product(getProductId(productXml.attributes).get,
        getItem(PRODUCT_NAME_XML_ELEMENT).get,
        getItem(PRODUCT_EAN_XML_ELEMENT).get,
        getItem(PRODUCT_LIST_PRICE_XML_ELEMENT).get.toDouble,
        getItem(PRODUCT_WHOLESALE_PRICE_XML_ELEMENT).get.toDouble)

      getCurrencyCode(productXml) match {
        case Some(currencyCode: String) if currencyCode != "" =>
          product.currency = Currency.getInstance(currencyCode)
        case Some(currencyCode: String) =>
          product.currency = Currency.getInstance(Product.DEFAULT_CURRENCY_CODE)
        case None =>
          return None
      }

      Some(product)
    }
    catch {
      case e@(_: NoSuchElementException | _: MatchError | _: NumberFormatException | _: IllegalArgumentException) => None
    }

  }

  private def getCurrencyCode(productXml: Node): Option[String] = {
    val listPriceCurrency = (productXml \ PRODUCT_LIST_PRICE_XML_ELEMENT \ PRODUCT_CURRENCY_XML_ATTRIB).text
    val wholesalePriceCurrency = (productXml \ PRODUCT_WHOLESALE_PRICE_XML_ELEMENT \ PRODUCT_CURRENCY_XML_ATTRIB).text

    if (listPriceCurrency.trim == wholesalePriceCurrency.trim) {
      Some(listPriceCurrency.trim)
    }
    else {
      None
    }
  }

  private def getItemFromXml(xmlNode: Node, itemName: String): Option[String] = {
    val itemNodeSeq = (xmlNode \ itemName)
    if (!itemNodeSeq.isEmpty) {
      Some(itemNodeSeq.text)
    }
    else {
      None
    }
  }

  private def getProductId(xmlAttributes: MetaData): Option[String] = {
    xmlAttributes.value match {
      case null =>
        None
      case _ =>
        xmlAttributes.value.length match {
          case 1 =>
            Some(xmlAttributes.value(0).text)
          case _ =>
            None
        }
    }
  }
}
