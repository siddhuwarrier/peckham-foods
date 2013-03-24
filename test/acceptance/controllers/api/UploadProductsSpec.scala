package acceptance.controllers.api

import _root_.controllers.api.ProductController
import acceptance._
import org.scalatest.{BeforeAndAfter, FeatureSpec}
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.GivenWhenThen.{Given, When, Then}

import play.api.test.Helpers.{await, running, OK}
import play.api.test.TestServer
import play.api.libs.ws.WS
import java.io.{ByteArrayOutputStream, File}
import org.apache.http.entity.mime.MultipartEntity
import org.apache.http.entity.mime.content.FileBody
import models.Product
import scala.collection.JavaConverters._
import play.api.http.HeaderNames
import org.codehaus.jackson.map.ObjectMapper
import org.codehaus.jackson.`type`.TypeReference
import play.api.libs.json.Json

class UploadProductsSpec extends FeatureSpec with ShouldMatchers with BeforeAndAfter {

  protected def app = TestServer(acceptance.TEST_SERVER_PORT)

  feature("Upload Catalogue data") {
    info("As a stockist at Peckham Foods")
    info("I want to upload my product catalogue")
    info("So that I can see what products I have for sale")

    scenario("It should be possible to upload a valid stock list catalogue") {
      running(app) {
        Given("I have a valid non-empty stock list catalogue")
        val file = new File(UploadProductsSpec.VALID_SAMPLE_STOCKLIST_FILE)

        When("I upload the stock list catalogue")
        val (outputStream, header) = UploadProductsSpec.buildMultipartRequest(file)
        val result = await(WS.url(UploadProductsSpec.UPLOAD_URL).withHeaders(header).put(outputStream.toByteArray))

        Then("I should see all the products in the catalogue")
        result.status should be(OK)

        result.header(HeaderNames.CONTENT_TYPE).get should be("application/json")

        val jsonObjectMapper = new ObjectMapper()
        val json = Json.stringify((Json.parse(result.body) \ "products"))
        val productsJson: java.util.List[String] = jsonObjectMapper
          .readValue(json, new TypeReference[java.util.List[String]]() {})
        val products = productsJson.asScala
          .foldLeft(List[Product]())((products, productJson) =>
          jsonObjectMapper.readValue(productJson, classOf[Product]) :: products)

        products.size should be(Product.find.all().size())
        products.foreach(product => Product.find.byId(product.productId) should not be (null))
      }
    }

    scenario("It should be possible to upload an empty stock list catalogue") {
      running(app) {
        Given("I have a empty stock list catalogue")
        val file = new File(UploadProductsSpec.EMPTY_SAMPLE_STOCKLIST_FILE)

        When("I upload the stock list catalogue")
        val (outputStream, header) = UploadProductsSpec.buildMultipartRequest(file)
        val result = await(WS.url(UploadProductsSpec.UPLOAD_URL).withHeaders(header).put(outputStream.toByteArray))

        Then("I should see that nothing has been uploaded")
        result.status should be(OK)
        result.header(HeaderNames.CONTENT_TYPE).get should be("application/json")

        val jsonObjectMapper = new ObjectMapper()
        val json = Json.stringify((Json.parse(result.body) \ "products"))
        val productsJson: java.util.List[String] = jsonObjectMapper
          .readValue(json, new TypeReference[java.util.List[String]]() {})

        productsJson.size() should equal(0)
      }
    }

    scenario("Upload stock should be idempotent") {
      running(app) {
        Given("I have already uploaded a catalogue")
        val file = new File(UploadProductsSpec.VALID_SAMPLE_STOCKLIST_FILE)
        val (outputStream, header) = UploadProductsSpec.buildMultipartRequest(file)
        val result = await(WS.url(UploadProductsSpec.UPLOAD_URL).withHeaders(header).put(outputStream.toByteArray))

        val numRecordsInDb = Product.find.findRowCount()

        When("I reupload the catalogue")
        val secondResult = await(WS.url(UploadProductsSpec.UPLOAD_URL).withHeaders(header).put(outputStream.toByteArray))

        Then("I should see that nothing has changed")
        result.body should equal (secondResult.body)
        Product.find.findRowCount() should equal (numRecordsInDb)
      }
    }

    scenario("Invalid products in the stock list should not be uploaded") {
      running(app) {
        Given("I have a stock list catalogue with a mix of valid and invalid products")
        val file = new File(UploadProductsSpec.INVALID_SAMPLE_STOCKLIST_FILE)

        When("I upload the stock list catalogue")
        val (outputStream, header) = UploadProductsSpec.buildMultipartRequest(file)
        val result = await(WS.url(UploadProductsSpec.UPLOAD_URL).withHeaders(header).put(outputStream.toByteArray))

        Then("I should see that only the valid products have been uploaded")
        result.status should be(OK)
        result.header(HeaderNames.CONTENT_TYPE).get should be("application/json")

        val jsonObjectMapper = new ObjectMapper()
        val json = Json.stringify((Json.parse(result.body) \ "products"))
        val productsJson: java.util.List[String] = jsonObjectMapper
          .readValue(json, new TypeReference[java.util.List[String]]() {})

        productsJson.size() should equal(UploadProductsSpec.NUM_VALID_PRODUCTS_IN_INVALID_SAMPLE_STOCKLIST_FILE)
        Product.find.findRowCount() should equal (productsJson.size())

      }
    }
  }

  after {
    running(app) {
      Product.find.all().asScala.map(product => product.delete())
    }
  }
}

object UploadProductsSpec {
  val UPLOAD_URL = "http://localhost:" + acceptance.TEST_SERVER_PORT + "/api/v1/products/"
  val VALID_SAMPLE_STOCKLIST_FILE = "test/resources/stocklist_xml/valid_sample_stocklist.xml"
  val INVALID_SAMPLE_STOCKLIST_FILE = "test/resources/stocklist_xml/invalid_sample_stocklist.xml"
  val EMPTY_SAMPLE_STOCKLIST_FILE = "test/resources/stocklist_xml/empty_sample_stocklist.xml"
  val NUM_VALID_PRODUCTS_IN_INVALID_SAMPLE_STOCKLIST_FILE = 2

  def buildMultipartRequest(file: File): (ByteArrayOutputStream, (String, String)) = {
    val entity = new MultipartEntity()
    entity.addPart(ProductController.PRODUCT_CATALOGUE_FILE_NAME, new FileBody(file))
    val outputStream = new ByteArrayOutputStream
    entity.writeTo(outputStream)
    val header = (entity.getContentType.getName, entity.getContentType.getValue)

    (outputStream, header)
  }
}


