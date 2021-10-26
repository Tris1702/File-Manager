package com.example.file_manager.activity.listFile

interface OnBackPressed {
    fun onClick()
    fun isClosed(): Boolean
}