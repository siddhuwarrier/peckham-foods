package models.factory

import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers

/**
 * Copyright (c) Cisco systems 2013. All rights reserved.
 * Date: 23/03/2013
 */
class StocklistFactorySpec extends FunSpec with ShouldMatchers {

  describe("The stock list factory") {
    it("should build a list of products from a stocklist XML equal to the number of products in the stocklist") {
      pending
    }

    it("should build an empty list of products if the stocklist XML is empty") {
      pending
    }

    it("should build a list of products from a stocklist XML that excludes all product entries that are malformed in the XML") {
      pending
    }
  }

}
