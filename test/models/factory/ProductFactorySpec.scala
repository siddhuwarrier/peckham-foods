package models.factory

import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers

/**
 * Copyright (c) Cisco systems 2013. All rights reserved.
 * Date: 23/03/2013
 */
class ProductFactorySpec extends FunSpec with ShouldMatchers {
  describe("The Product Factory") {
    it("should build a product with the details extracted from an XML entity") {
      pending
    }

    it("should not build a product if the XML entity is empty") {
      pending
    }

    it("should not build a product if the name is missing") {
      pending
    }

    it("should not build a product if the EAN is missing") {
      pending
    }

    it("should not build a product if the list price is missing") {
      pending
    }

    it("should not build a product if the wholesale price is missing") {
      pending
    }
  }
}
