package unit.controllers

import play.api.test.Helpers._
import play.api.test.FakeRequest
import play.api.test.FakeApplication
import scala.Some
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{BeforeAndAfter, FunSpec}

/**
 * Copyright (c) Cisco systems 2013. All rights reserved.
 * Date: 21/03/2013
 */
class ApplicationSpec extends FunSpec with ShouldMatchers with BeforeAndAfter {

  before {
    running(FakeApplication()) {
    }
  }

  it("should redirect the user to the page immediately if he has a valid credential cookie set") {
    val Some(result) = routeAndCall(FakeRequest(GET, "/"))
    status(result) should equal (OK)
  }
}
