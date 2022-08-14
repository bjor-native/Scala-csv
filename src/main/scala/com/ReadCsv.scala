package com

import scala.io.BufferedSource


object ReadCsv {

  def salesByRegion(region: String): Int = {
    val bufferedSource: BufferedSource = io.Source
      .fromFile("/home/arcateon/IdeaProjects/Scala-csv/src/main/scala/source/testData.csv")
      var sum = 0
      for (line <- bufferedSource.getLines()) {
        if (line.contains(region)) {
          val cols = line.split(",").map(_.trim)
          val res = cols(0).substring(1, cols(0).length -1).toInt
          sum += res
        }
      }
      sum
    }

}


