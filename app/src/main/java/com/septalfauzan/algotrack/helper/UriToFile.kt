package com.septalfauzan.algotrack.helper

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import java.io.File
import java.io.IOException
import kotlin.jvm.Throws

@Throws(IOException::class)
fun uriToFile(context: Context, uri: Uri): File {
    val contentResolver = context.contentResolver
    val filePath = getRealPathFromURI(contentResolver, uri)
    return File(filePath)
}

private fun getRealPathFromURI(contentResolver: ContentResolver, uri: Uri): String {
    val projection = arrayOf(android.provider.MediaStore.Images.Media.DATA)
    val cursor = contentResolver.query(uri, projection, null, null, null)
    val columnIndex = cursor?.getColumnIndexOrThrow(android.provider.MediaStore.Images.Media.DATA)
    cursor?.moveToFirst()
    val filePath = cursor?.getString(columnIndex ?: -1)
    cursor?.close()
    return filePath ?: ""
}