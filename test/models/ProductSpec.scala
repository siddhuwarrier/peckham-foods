package models

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FunSpec
import builders.ProductBuilder
import play.api.test.Helpers.running
import play.api.test.FakeApplication

class ProductSpec extends FunSpec with ShouldMatchers {

  protected def fakeApp = FakeApplication(additionalConfiguration = Map(
    "db.default.driver" -> "org.h2.Driver",
    "db.default.url" -> "jdbc:h2:mem:test;MODE=MySQL"
  ))

  describe("A product") {
    it("should be persisted to the database if all of the required fields are set") {

      running(fakeApp) {
        val product = ProductBuilder.build
        product.save()

        val productInDb = Product.find.byId(product.productId)

        productInDb should not be (null)
        productInDb should equal (product)
      }
    }

    it("should not be possible to find a product with an invalid product Id") {
      val nonExistentId = "non-existent-id"
      running(fakeApp) {
        Product.find.byId(nonExistentId) should be(null)
      }
    }

    it("should equal another product with the same product ID, name, ean, list price and wholesale price") {
      val product = ProductBuilder.build
      val anotherProduct = new Product(product.productId, product.productName, product.ean, product.listPrice, product.wholesalePrice)

      anotherProduct should equal (product)
    }

    it("should not equal another product with the different product ID") {
      val product = ProductBuilder.build
      val anotherProduct = new Product("another", product.productName, product.ean, product.listPrice, product.wholesalePrice)

      anotherProduct should not equal (product)
    }

    it("should not equal another product with the same product name") {
      val product = ProductBuilder.build
      val anotherProduct = new Product(product.productId, "evil", product.ean, product.listPrice, product.wholesalePrice)

      anotherProduct should not equal (product)
    }
  }
}
