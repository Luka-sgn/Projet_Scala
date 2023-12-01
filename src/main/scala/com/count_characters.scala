package com

import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Row, SparkSession}

class count_characters(spark: SparkSession) {

  def countCharactersInDataFrame(df: DataFrame, textColumnName: String): DataFrame = {
    // Transformation de chaque ligne de texte en une séquence de caractères
    val charRDD = df.rdd.flatMap(row => row.getAs[String](textColumnName).toList.map(char => Row(char.toString)))

    // Définition le schéma pour le DataFrame des caractères
    val schema = StructType(Array(StructField("character", StringType, nullable = true)))

    // Création un DataFrame de caractères
    val charactersDF = spark.createDataFrame(charRDD, schema)

    // Comptage des occurrences des caractères
    charactersDF.groupBy("character").agg(count("character").alias("count")).orderBy(desc("count"))
  }
}