package com.eaapps.schoolsguide.utils

import android.content.Context
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object FileHelper {

    fun writeTempFileFromUri(context: Context, uri: Uri, ext: String = ".png"): File {
        // val id = DocumentsContract.getDocumentId(uri)
        val input = context.contentResolver?.openInputStream(uri)
        val file = File(context.cacheDir.absolutePath + "/" + "tempUpload"+(0 until 1000).random() + ext)
        return file.apply {
            FileOutputStream(file).apply {
                try {
                    val buf = ByteArray(input?.available()!!)
                    var len: Int = input.read(buf)
                    while (len > 0) {
                        this.write(buf, 0, len)
                        len = input.read(buf)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    try {
                        this.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }


            }
        }
    }

    fun multiPartFile(file: File,name:String) : MultipartBody.Part{
        return file.let {
            val requestFile = it.asRequestBody("multipart/form-data; charset=utf-8".toMediaTypeOrNull())
            MultipartBody.Part.createFormData(name, filename = it.name, requestFile)
        }
    }

}