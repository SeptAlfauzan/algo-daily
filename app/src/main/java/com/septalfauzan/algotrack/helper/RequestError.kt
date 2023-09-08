package com.septalfauzan.algotrack.helper

import com.google.gson.GsonBuilder
import com.septalfauzan.algotrack.domain.model.ErrorResponse

object RequestError {
    fun String.getErrorMessage(): ErrorResponse{
        return try {
            val gson = GsonBuilder()
                .setLenient()
                .create()
            gson.fromJson(this, ErrorResponse::class.java)
        }catch (e: Exception){
            e.printStackTrace()
            ErrorResponse(errors = this)
        }
    }
}