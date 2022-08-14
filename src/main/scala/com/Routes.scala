package com

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import com.cases.{SalesByIdError, SalesOfRegion, SalesOfRegionError}
import org.json4s.Formats
import org.json4s.jackson._


object Routes {

  val route: Route = {

    implicit val formats: Formats = org.json4s.DefaultFormats.withLong.withDouble.withStrictOptionParsing

    // sample request: /total-sales/?region=московская, /sales/?region=Чувашия

    pathPrefix("total-sales") {
      parameters("region") { (region) =>
        if (ReadCsv.salesByRegion(region) != 404) {
          val response = SalesOfRegion(region, ReadCsv.salesByRegion(region.capitalize))
          val responseJson = Serialization.write(response)
          complete(responseJson)
        } else {
          val response = SalesOfRegionError("File with data does not exist")
          val responseJson = Serialization.write(response)
          complete(responseJson)
        }
      }
    }

    // sample request: /data-id/?id=3

    pathPrefix("data-id") {
      parameters("id") { (id) =>
        if (ReadCsv.salesById(id) != "404" && ReadCsv.salesById(id).nonEmpty) {
          complete(ReadCsv.salesById(id))
        } else if (ReadCsv.salesById(id).isEmpty) {
          complete(Serialization.write(SalesByIdError(id, "Invalid id")))
        } else complete("Server error")
      }
    }
  }
}


