package com

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import com.cases.SalesOfRegion
import org.json4s.Formats
import org.json4s.jackson._


object Routes {

  val route: Route = {

    implicit val formats: Formats = org.json4s.DefaultFormats.withLong.withDouble.withStrictOptionParsing

    // sample request: /sales/?region=московская, /sales/?region=Чувашия

    pathPrefix("sales") {
      parameters("region") { (region) =>
        val response = SalesOfRegion(region, ReadCsv.salesByRegion(region.capitalize))
        val responseJson = Serialization.write(response)
        complete(responseJson)
      }
    }
  }
}


