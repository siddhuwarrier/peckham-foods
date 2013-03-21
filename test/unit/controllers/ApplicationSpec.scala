package unit.controllers

import play.api.test.Helpers._
import play.api.test.FakeRequest
import play.api.test.FakeApplication
import scala.Some
import org.specs2.mutable._

/**
 * Copyright (c) Cisco systems 2013. All rights reserved.
 * Date: 21/03/2013
 */
class ApplicationSpec extends Specification {
  "The Application Controller" should {
    "show the default index page when the user visits it" in {
      running(FakeApplication()) {
        val Some(result) = routeAndCall(FakeRequest(GET, "/"))
        status(result) must beEqualTo(OK)
      }
    }
  }
}
