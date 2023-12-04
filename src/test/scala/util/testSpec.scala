package util

import com.{count_characters, read_text_folder}
import org.apache.spark.sql.{Row, SparkSession}

import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.scalatest.funsuite.AnyFunSuite


class testSpec extends AnyFunSuite {
  test("TextToDataFrame") {
    val spark = SparkSession.builder
      .appName("Test")
      .master("local")
      .getOrCreate()

    val readTextFolder = new read_text_folder()
    val df = readTextFolder.TextToDataFrame()
    spark.stop()
  }
  test("CountCharacters") {
    val spark = SparkSession.builder
      .appName("CharacterCountTest")
      .master("local")
      .getOrCreate()

    // Création d'une DataFrame de test avec createDataFrame
    val schema = StructType(Array(StructField("text", StringType, nullable = true)))
    val data = Seq(Row("exemple de chaîne de caractères"))
    val rdd = spark.sparkContext.parallelize(data)
    val testDF = spark.createDataFrame(rdd, schema)

    // Utilisation de la classe CountCharacters pour compter les caractères
    val counter = new count_characters(spark)
    val resultDF = counter.countCharactersInDataFrame(testDF, "text")

    // Assertions pour le test
    assert(resultDF.count() > 0, "Le DataFrame résultant ne devrait pas être vide.")
    resultDF.show(false)
    spark.stop()
  }
}