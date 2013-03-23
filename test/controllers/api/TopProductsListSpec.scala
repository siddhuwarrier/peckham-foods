package controllers.api

import org.scalatest.FeatureSpec
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.GivenWhenThen.{Given, When, Then}

class TopProductsListSpec extends FeatureSpec with ShouldMatchers {
  feature("List my best selling products") {
    info("As a Business Analyst")
    info("I want to get a list of the top n products by number sold")
    info("So that I can see which of my lines are most popular")

    scenario("Display top 10 products by default") {
      Given("I have 10 or more products in my store database")

      When("I retrieve the top products by number sold")

      Then("I should see the top 10 products by number sold")

      pending
    }
  }
}
