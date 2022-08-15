package com

import com.cases.{AddToCsvAnswer, DataListOfRegion, SalesById}
import com.github.tototoshi.csv.CSVWriter
import org.json4s.Formats
import org.json4s.jackson.Serialization
import java.io.FileNotFoundException
import scala.io.BufferedSource
import scala.collection.mutable.ArrayBuffer


object ReadCsv extends App{

  implicit val formats: Formats = org.json4s.DefaultFormats.withLong.withDouble.withStrictOptionParsing

  def salesByRegion(region: String): Int = {
    try {
      val bufferedSource: BufferedSource = io.Source
        .fromFile("/home/arcateon/IdeaProjects/Scala-csv/src/main/scala/source/testData.csv")
      var sum = 0
      for (line <- bufferedSource.getLines()) {
        if (line.contains(region)) {
          val cols = line.split(",").map(_.trim)
          val res = cols(0).toInt
          sum += res
        }
      }
      sum
    } catch {
      case _: FileNotFoundException =>
        val fileNotFound = 404
        fileNotFound
    }
  }


  def salesById(id: String): String = {

    try {
      val bufferedSource: BufferedSource = io.Source
        .fromFile("/home/arcateon/IdeaProjects/Scala-csv/src/main/scala/source/testData.csv")
      var resJson: String = ""

      for (line <- bufferedSource.getLines.drop(1)) {
        val cols = line.split(",").map(_.trim)
        if (cols(3) == id) {
          val sales = cols(0)
          val index = cols(1)
          val region = cols(2)
          val id = cols(3)

          resJson = Serialization.write(SalesById(region, index, sales, id))
        }
      }
      resJson
    } catch {
      case _: FileNotFoundException =>
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
          val resultString = s"{sales:${cols(0)}" +
            s", index:${cols(1)}" +
            s", region:${cols(2)}" +
            s", id:${cols(3)}}"
          resultArray += resultString
          resultJson = Serialization.write(DataListOfRegion(resultArray))
        }
      }
      resultJson
    } catch {
      case _: FileNotFoundException =>
        "404"
    }
  }

  def dataListById(id: String): String = {
    try {
      val bufferedSource: BufferedSource = io.Source
        .fromFile("/home/arcateon/IdeaProjects/Scala-csv/src/main/scala/source/testData.csv")

      var resultJson = ""
      val resultArray = ArrayBuffer[String]()

      for (line <- bufferedSource.getLines.drop(1)) {
        val cols = line.split(",").map(_.trim)
        if (cols(3).startsWith(id)) {
          val resultString = s"{sales:${cols(0)}" +
            s", index:${cols(1)}" +
            s", region:${cols(2)}" +
            s", id:${cols(3)}}"
          resultArray += resultString
          resultJson = Serialization.write(DataListOfRegion(resultArray))
        }
      }
      resultJson
    } catch {
      case _: FileNotFoundException =>
        "404"
    }
  }

  def dataListBySales(sales: String): String = {
    try {
      val bufferedSource: BufferedSource = io.Source
        .fromFile("/home/arcateon/IdeaProjects/Scala-csv/src/main/scala/source/testData.csv")

      var resultJson = ""
      val resultArray = ArrayBuffer[String]()

      for (line <- bufferedSource.getLines.drop(1)) {
        val cols = line.split(",").map(_.trim)
        if (cols(0).toInt > sales.toInt) {
          val resultString = s"{sales:${cols(0)}" +
            s", index:${cols(1)}" +
            s", region:${cols(2)}" +
            s", id:${cols(3)}}"
          resultArray += resultString
          resultJson = Serialization.write(DataListOfRegion(resultArray))
        }
      }
      resultJson
    } catch {
      case _: FileNotFoundException =>
        "404"
      case _: NumberFormatException =>
        "405"
    }
  }

  def writeDataToCsv(sales: Int, index: Int, region: String, id: Int): String = {

    val bufferedSource: BufferedSource = io.Source
      .fromFile("/home/arcateon/IdeaProjects/Scala-csv/src/main/scala/source/testData.csv")
    val idArr = ArrayBuffer[Int]()
    for (line <- bufferedSource.getLines.drop(1)) {
      val cols = line.split(",").map(_.trim)
      idArr += cols(3).toInt
    }

    var resultJson = ""
    if (!idArr.contains(id)) {
      val writer = CSVWriter
        .open("/home/arcateon/IdeaProjects/Scala-csv/src/main/scala/source/testData.csv"
          , append = true)

      writer.writeRow(List(sales, index, region, id))
      writer.close
      resultJson = Serialization.write(AddToCsvAnswer(success = true, "data added"))
      resultJson
    } else {
      resultJson = Serialization.write(AddToCsvAnswer(success = false, "id value is already exist"))
      resultJson
    }
  }

}




