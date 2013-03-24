package models.factory

import xml.{Node, Elem}
import models.Product

/**
 * Copyright (c) Cisco systems 2013. All rights reserved.
 * Date: 24/03/2013
 */
object StocklistFactory {

  val PRODUCT_XML_ELEMENT = "product"

  def build(xmlData: Elem): List[Product] = {
    val productElements = (xmlData \ PRODUCT_XML_ELEMENT)
    productElements.foldLeft(List[Product]())((products, productElement) => buildProduct(productElement, products))
  }

  private def buildProduct(productElement: Node, products: List[Product]): List[Product] = {
    val productOption = ProductFactory.build(productElement)
    productOption match {
      case Some(product: Product) => product :: products
      case None => products
    }
  }
}
