package acceptance.controllers.api

import org.scalatest.{BeforeAndAfter, FeatureSpec}
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.GivenWhenThen.{Given, When, Then}
import play.api.test.Helpers._
import models.Product
import acceptance.acceptance
import scala.collection.JavaConverters._
import builders.ProductBuilder
import play.api.test.TestServer
import play.api.libs.ws.WS
import loggers.TestLogger

/**
 * Copyright (c) Cisco systems 2013. All rights reserved.
 * Date: 01/04/2013
 */
class ViewProductSpec extends FeatureSpec with ShouldMatchers with BeforeAndAfter {
  protected def app = TestServer(acceptance.TEST_SERVER_PORT)

  feature("PF-22: View Catalogue data") {
    info("As a business analyst")
    info("I want to view the details of a particular product in the catalogue by ID")
    info("So that I can quickly find the product I want")

    scenario("PF-25: Should display stock item with a valid product ID") {
      running(app) {
        Given("I have uploaded stock to the application")
        val product = createSampleProduct()

        When("I choose to view a stock item that exists in the application")
        val result = await(WS.url(ViewProductSpec.VIEW_URL.format(product.productId)).get())

        Then("I should see the stock item")
        result.status should be(OK)
        TestLogger.debug(result.body)
      }
    }
  }

  def createSampleProduct() = {
    val product = ProductBuilder.build
    product.save()
    product
  }

  after {
    running(app) {
      Product.find.all().asScala.map(product => product.delete())
    }
  }
}

object ViewProductSpec {
  val VIEW_URL = "http://localhost:" + acceptance.TEST_SERVER_PORT + "/api/v1/products/%s"
}
