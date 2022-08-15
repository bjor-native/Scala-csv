package com

import com.cases.{DataListOfRegion, SalesById}
import org.json4s.Formats
import org.json4s.jackson.Serialization

import java.io.FileNotFoundException
import scala.io.BufferedSource
import scala.collection.mutable.ArrayBuffer


object ReadCsv {

  implicit val formats: Formats = org.json4s.DefaultFormats.withLong.withDouble.withStrictOptionParsing

  def salesByRegion(region: String): Int = {
    try {
      val bufferedSource: BufferedSource = io.Source
        .fromFile("/home/arcateon/IdeaProjects/Scala-csv/src/main/scala/source/testData.csv")
      var sum = 0
      for (line <- bufferedSource.getLines()) {
        if (line.contains(region)) {
          val cols = line.split(",").map(_.trim)
          val res = cols(0).substring(1, cols(0).length - 1).toInt
          sum += res
        }
      }
      sum
    } catch {
      case _: FileNotFoundException => println("File not found")
        val fileNotFound = 404
        fileNotFound
    }
    }


  def salesById(id: String): String = {

      try {
        val bufferedSource: BufferedSource = io.Source
          .fromFile("/home/arcateon/IdeaProjects/Scala-csv/src/main/scala/source/testData.csv")
        var resJson: String = ""

        for (line <- bufferedSource.getLines()) {
          val cols = line.split(",").map(_.trim)
          if (cols(3).substring(1, cols(3).length - 1) == id) {
            val sales = cols(0).substring(1, cols(0).length - 1)
            val index = cols(1).substring(1, cols(1).length - 1)
            val region = cols(2).substring(1, cols(2).length - 1)
            val id = cols(3).substring(1, cols(3).length - 1)

            resJson = Serialization.write(SalesById(region, index, sales, id))
          }
        }
        resJson
      } catch {
        case _: FileNotFoundException => println("File not found")
          "404"
      }
  }

  def dataListByRegion(region: String): String = {
    try {
      val bufferedSource: BufferedSource = io.Source
        .fromFile("/home/arcateon/IdeaProjects/Scala-csv/src/main/scala/source/testData.csv")

      var resultJson = ""
      val resultArray = ArrayBuffer[String]()

      for (line <- bufferedSource.getLines()) {
        if (line.contains(region.capitalize)) {
          val cols = line.split(",").map(_.trim)
          val resultString = s"{sales:${cols(0).substring(1, cols(0).length - 1)}" +
            s", index:${cols(1).substring(1, cols(1).length - 1)}" +
            s", region:${cols(2).substring(1, cols(2).length - 1)}" +
            s", id:${cols(3).substring(1, cols(3).length - 1)}}"
          resultArray += resultString
          resultJson = Serialization.write(DataListOfRegion(resultArray))
        }
      }
      resultJson
    } catch {
      case _: FileNotFoundException => println("File not found")
        "404"
    }
  }
}


