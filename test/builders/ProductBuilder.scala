package builders

import models.Product

object ProductBuilder {
  private val PRODUCT_ID = "pid-268542796"
  private val PRODUCT_NAME = ""
  private val EAN = ""
  private val LIST_PRICE = 2.6d
  private val WHOLESALE_PRICE = 2.5d

  def build: Product = new Product(PRODUCT_ID, PRODUCT_NAME, EAN, LIST_PRICE, WHOLESALE_PRICE)

}
