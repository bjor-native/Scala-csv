package com

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import org.json4s.Formats
import org.json4s.jackson._


object Routes {

  val route: Route = {

    // sample request: /sales/?region=московская, /sales/?region=Чувашия

    path ("sales") {
      parameters("region") { (region) =>
        complete(s"Sales of ${region}: ${ReadCsv.salesByRegion(region.capitalize)}")
      }
    }
  }
}


