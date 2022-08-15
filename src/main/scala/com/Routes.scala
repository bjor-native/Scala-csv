package com

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import com.cases.{DataListBySalesError, SalesByIdError, SalesOfRegion, SalesOfRegionError}
import org.json4s.Formats
import org.json4s.jackson._


object Routes {

  val route: Route = {

    implicit val formats: Formats = org.json4s.DefaultFormats.withLong.withDouble.withStrictOptionParsing

    // sample request: /total-sales/?region=московская, /sales/?region=Чувашия
    pathPrefix("total-sales") {
      parameters("region") { (region) =>
        if (ReadCsv.salesByRegion(region.capitalize) != 404
          && ReadCsv.salesByRegion(region.capitalize) != 0) {
          val response = SalesOfRegion(region, ReadCsv.salesByRegion(region.capitalize))
          val responseJson = Serialization.write(response)
          complete(responseJson)
        } else if (ReadCsv.salesByRegion(region) == 0) {
          val response = SalesOfRegionError(region, message = "invalid region")
          val responseJson = Serialization.write(response)
          complete(responseJson)
        } else complete("Server error")
      }
    } ~      // sample request: /data-id/?id=3
      pathPrefix("data-id") {
        parameters("id") { (id) =>
          if (ReadCsv.salesById(id) != "404" && ReadCsv.salesById(id).nonEmpty) {
            complete(ReadCsv.salesById(id))
          } else if (ReadCsv.salesById(id).isEmpty) {
            complete(Serialization.write(SalesByIdError(id, "invalid id")))
          } else complete("Server error")
        }
      } ~   // sample request: /data-list/?region=москва, /data-list/?region=Чувашия
      pathPrefix("data-list-region") {
        parameters("region") { (region) =>
          if (ReadCsv.dataListByRegion(region) != "404" && ReadCsv.dataListByRegion(region) != "") {
            complete(ReadCsv.dataListByRegion(region))
          } else if (ReadCsv.dataListByRegion(region) == "") {
            complete(Serialization.write(SalesOfRegionError(region, message = "invalid region")))
          } else complete("Server error")
        }
      } ~   // sample request: /data-list-id/?start-with=30
      pathPrefix("data-list-id") {
        parameters("start-with") { (id) =>
          if (ReadCsv.dataListById(id) != "404" && ReadCsv.dataListById(id) != "") {
            complete(ReadCsv.dataListById(id))
          } else if (ReadCsv.dataListById(id) == "") {
            complete(Serialization.write(SalesByIdError(id, "invalid id")))
          } else complete("Server error")
        }
      } ~   // sample request: /data-list-sales/?more-than=5000
      pathPrefix("data-list-sales") {
        parameters("more-than") { (sales) =>
          if (ReadCsv.dataListBySales(sales) != "404" && ReadCsv.dataListBySales(sales) != "") {
            complete(ReadCsv.dataListBySales(sales))
          } else if (ReadCsv.dataListBySales(sales) == "") {
            complete(Serialization.write(DataListBySalesError(sales, message = "invalid sales")))
          } else complete("Server error")
        }
      }
  }
}


