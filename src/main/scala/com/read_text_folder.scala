package com

import org.apache.spark.sql.{SparkSession, DataFrame, Row}
import org.apache.spark.sql.types.{StructType, StructField, StringType}
import scala.util.Random
import java.io.File

case class read_text_folder() {
  def TextToDataFrame(): DataFrame = {
    // Configuration de Spark
    val spark = SparkSession.builder.appName("TextToDataFrameExample").getOrCreate()
    // cas ou on a un fichier en cours d'utilisation et un fichier non utilisé dans le noeud.
    // Utilisation de FileSelector pour filtrer les fichiers
    val fileSelector = new FileSelector()
    val prohibitedWords = List("mort", "panne", "utilisation")
    val directoryPath1 = "H:/Desktop/spark_courses_project/Projet_Scala/src/noeud(1)/"
    val directoryPath2 = "H:/Desktop/spark_courses_project/Projet_Scala/src/noeud(2)/"
    val FilterededFiles1 = fileSelector.FilterFiles(directoryPath1, prohibitedWords)
    val FilterededFiles2 = fileSelector.FilterFiles(directoryPath2, prohibitedWords)

    if (FilterededFiles1.isEmpty && FilterededFiles2.isEmpty) {
      val endutilfile = new FileRenamer()
      endutilfile.rollBackFileName_utilisation("H:/Desktop/spark_courses_project/Projet_Scala/src/noeud(1)/", "utilisation", "1")
      endutilfile.rollBackFileName_utilisation("H:/Desktop/spark_courses_project/Projet_Scala/src/noeud(1)/", "utilisation", "2")
      endutilfile.rollBackFileName_utilisation("H:/Desktop/spark_courses_project/Projet_Scala/src/noeud(2)/", "utilisation", "3")
      endutilfile.rollBackFileName_utilisation("H:/Desktop/spark_courses_project/Projet_Scala/src/noeud(2)/", "utilisation", "4")
    }
    val excludedWords = List("mort", "panne")
    var FilterededFiles = fileSelector.FilterFiles(directoryPath1, excludedWords)
    // Logique de sélection et de rechargement des fichiers
    if (FilterededFiles.isEmpty) {
      //cas ou on a aucun fichier valide dans le noeud1
      println("Aucun fichier valide trouvé dans le noeud(1), recherche dans le noeud(2).")
      FilterededFiles = fileSelector.FilterFiles(directoryPath2, excludedWords)

      if (FilterededFiles.isEmpty) {
        // cas ou on a aucun fichier valide dans le noeud 2
        val randomChoice = Random.nextInt(2)
        if (randomChoice == 0) {
          // on recharge ici les fichiers du noeud 1 pour qu'ils soient de nouveau utilisable
          val rebootfile = new FileRenamer()
          println("Rechargement des fichiers du noeud(1)")
          rebootfile.rollBackFileName("H:/Desktop/spark_courses_project/Projet_Scala/src/noeud(1)/","1")
          rebootfile.rollBackFileName("H:/Desktop/spark_courses_project/Projet_Scala/src/noeud(1)/","2")
        } else {
          // on recharge ici les fichiers du noeud 2 pour qu'ils soient de nouveau utilisable
          println("Rechargement des fichiers du noeud(2)")
          val rebootfile2 = new FileRenamer()
          rebootfile2.rollBackFileName("H:/Desktop/spark_courses_project/Projet_Scala/src/noeud(2)/", "3")
          rebootfile2.rollBackFileName("H:/Desktop/spark_courses_project/Projet_Scala/src/noeud(2)/", "4")
        }
        FilterededFiles = fileSelector.FilterFiles(directoryPath1, excludedWords)

        if (FilterededFiles.isEmpty) {
          // cas ou le rechargement des 2 noeuds a échoué
          println("Aucun fichier valide trouvé dans le noeud(1), recherche dans le noeud(2).")
          FilterededFiles = fileSelector.FilterFiles(directoryPath2, excludedWords)
        }
      }
    }


    if (FilterededFiles.nonEmpty) {
      val validFile = FilterededFiles.head
      val fileRenamer = new FileRenamer()
      val originalName = validFile.getName
      println(s"Le fichier utilisé pour le traitement est ${validFile}.")
      val newName = fileRenamer.renameFileRandomly(validFile.getParent, originalName)

      // Assurez-vous que le nouveau nom est utilisé pour le chemin complet
      val validFilePath = new File(validFile.getParent, newName).getAbsolutePath

      val schema = StructType(Array(StructField("text", StringType, true)))
      val textRDD = spark.sparkContext.textFile(validFilePath)
      val textDF = spark.createDataFrame(textRDD.map(r => Row(r)), schema)

      textDF
    } else {
      throw new Exception("Aucun fichier valide trouvé après les tentatives de rechargement.")
    }
  }
}