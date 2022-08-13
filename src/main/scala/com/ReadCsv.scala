package com

import scala.io.BufferedSource


object ReadCsv {

  case class SalesData(sales: Int, index: Int, region: String, id: Int)

  def salesByRegion(region: String): Int = {
    val bufferedSource: BufferedSource = io.Source
      .fromFile("/home/arcateon/IdeaProjects/Scala-csv/src/main/scala/source/testData.csv")
      var sum = 0
      for (line <- bufferedSource.getLines()) {
        if (line.contains(region)) {
          val cols = line.split(",").map(_.trim)
          val res = cols(1).substring(1, cols(1).length -1).toInt
          sum += res
        }
      }
      sum
    }

}


