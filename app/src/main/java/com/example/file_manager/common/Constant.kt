package com.example.file_manager.common

object Constant {
    var musicEx = mutableListOf(".mp3", ".wav")
    var videoEx = mutableListOf(".mp4", ".avi", ".mpe")
    var wordEx = mutableListOf(".doc", ".docx")
    var plainTextEx = mutableListOf(".txt")
    var pdfEx = mutableListOf(".pdf")
    var pptEx = mutableListOf(".ppt", "pptx")
    var excelEx = mutableListOf(".xls", ".xlsx")
    var compressEx = mutableListOf(".zip", ".rar")
    var imageEx = mutableListOf(".png", ".jpg",".jpeg")
    var gifEx = mutableListOf(".gif")
    var docEx = mutableListOf(".doc", ".docx", ".txt", ".pdf", ".ppt", "pptx", ".xls", ".xlsx")
    var path = ""
    var pathDownload = ""

    enum class TYPE_FOLDER{
        IMAGE_FOLDER,
        DOCUMENT_FOLDER,
        AUDIO_FOLDER,
        VIDEO_FOLDER,
        DOWNLOAD_FOLDER,
        OTHER_FOLDER
    }
}