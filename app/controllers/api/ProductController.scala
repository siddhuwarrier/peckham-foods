package controllers.api

import play.api.mvc.{Action, Controller}
import java.io.{File, InputStreamReader, FileInputStream}
import models.factory.StocklistFactory
import play.api.libs.json.{JsValue, Json}
import models.Product
import scala.collection.JavaConverters._
import org.codehaus.jackson.map.ObjectMapper
import play.api.http.{MimeTypes, HeaderNames}

/**
 * Copyright (c) Cisco systems 2013. All rights reserved.
 * Date: 24/03/2013
 */


object ProductController extends Controller {
  val PRODUCT_CATALOGUE_FILE_NAME = "catalogue"

  def upload = Action(parse.multipartFormData) {
    implicit request =>
      val uploadedData = request.body.file(PRODUCT_CATALOGUE_FILE_NAME)
      uploadedData.map {
        filePart =>
          try {
            processAndSaveCatalogue(filePart.ref.file)
          }
          catch {
            case _ =>
              onUploadFail
          }

          Redirect("/api/products/", SEE_OTHER)
      }
        .getOrElse {
        onUploadFail
      }

  }

  private def onUploadFail = {
    BadRequest("Cannot parse your request")
  }

  private def processAndSaveCatalogue(file: File) {
    val xmlData = scala.xml.XML.load(new InputStreamReader(new FileInputStream(file)))
    val products = StocklistFactory.build(xmlData)
    products.map(product => product.save())
  }

  def showAll = Action {
    val products = Product.find.all.asScala
    val objectMapper = new ObjectMapper()

    Ok(Json.toJson(Map("products" -> products.map(product => objectMapper.writeValueAsString(product)))))
      .withHeaders(CONTENT_TYPE -> MimeTypes.JSON)
  }

  def showOne(productId: String) = Action {
    val objectMapper = new ObjectMapper()

    Ok(objectMapper.writeValueAsString(Product.find.byId(productId)))
  }
}
