package com
import java.io.File
import scala.util.Random

class FileRenamer {
  def renameFileRandomly(directory: String, oldName: String): String = {
    val randomSuffix = List("mort", "panne", "utilisation", "utilisationt")(Random.nextInt(4))
    val newName = oldName.stripSuffix(".txt") + s"($randomSuffix).txt"
    val sourceFile = new File(directory, oldName)
    val destFile = new File(directory, newName)

    if (sourceFile.exists() && sourceFile.renameTo(destFile)) {
      println("Le fichier utilisé est desormais indisponnible pour cause de " +s"$randomSuffix" +" du fichier.")
      newName
    } else {
      throw new Exception("erreur statut du fichier incertain")
    }
  }

  def rollBackFileName(directory: String, includedWords: String): Unit = {
    val fileSelector = new FileSelector()
    val selectedFiles = fileSelector.SelectFiles(directory, includedWords)

    selectedFiles.foreach { file =>
      val newName = "rapport(" + includedWords + ").txt"
      val destFile = new File(directory, newName)

      if (file.exists() && file.renameTo(destFile)) {
        println("Le fichier rapport(" + includedWords + ").txt " + "est desormais disponnible")
      } else {
        println("erreur statut du fichier incertain")
      }
    }
  }

  def rollBackFileName_utilisation(directory: String, includedWords_utilisation: String, includedWords:String): Unit = {
    val fileFilterUtil = new FileSelector()
    val selectedFiles_util = fileFilterUtil.SelectFiles_utilisation(directory, includedWords_utilisation, includedWords)
    if (selectedFiles_util.nonEmpty) {
      selectedFiles_util.foreach { file =>
      val newName = "rapport(" + includedWords + ").txt"
      val destFile = new File(directory, newName)

      if (file.exists() && file.renameTo(destFile)) {
        println("Le fichier rapport(" + includedWords + ").txt " + "est desormais disponnible car son utilsation est terminée")
      } else {
        println("erreur statut du fichier incertain")
      }
    }
    }
  }
}