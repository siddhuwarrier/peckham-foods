package models

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FunSpec
import builders.ProductBuilder
import play.api.test.Helpers.running
import play.api.test.FakeApplication

/**
 * Copyright (c) Cisco systems 2013. All rights reserved.
 * Date: 23/03/2013
 */
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
        productInDb should equal(product)
      }
    }

    it("should not be possible to find a product with an invalid product Id") {
      val nonExistentId = "non-existent-id"
      running(fakeApp) {
        Product.find.byId(nonExistentId) should be (null)
      }
    }
  }
}
