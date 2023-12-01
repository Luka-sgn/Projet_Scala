package com
import java.io.File
import scala.collection.mutable.ListBuffer

class FileSelector {
    def FilterFiles(directoryPath: String, excludedWords: List[String]): List[File] = {
      val dir = new File(directoryPath)
      val selectedFiles = ListBuffer[File]()

      if (dir.exists && dir.isDirectory) {
        dir.listFiles
          .filter(file => !excludedWords.exists(word => file.getName.contains(word)))
          .foreach(selectedFiles += _)
      }
      selectedFiles.toList
    }

  def SelectFiles(directoryPath: String, includedWords: String): List[File] = {
    val dir = new File(directoryPath)
    val selectedFiles = ListBuffer[File]()

    if (dir.exists && dir.isDirectory) {
      dir.listFiles
        .filter(file => file.getName.contains(includedWords))
        .foreach(selectedFiles += _)
    }
    selectedFiles.toList
  }

  def SelectFiles_utilisation(directoryPath: String, includedWord_utilisation: String, includedWords: String): List[File] = {
    val dir = new File(directoryPath)
    val selectedFiles = ListBuffer[File]()

    if (dir.exists && dir.isDirectory) {
      dir.listFiles
        .filter(file => file.getName.contains(includedWord_utilisation) &&  file.getName.contains(includedWords))
        .foreach(selectedFiles += _)
    }
    selectedFiles.toList
  }

}