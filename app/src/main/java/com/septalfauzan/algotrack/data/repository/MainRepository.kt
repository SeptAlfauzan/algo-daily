package com.septalfauzan.algotrack.data.repository

import android.content.Context
import com.google.gson.Gson
import com.septalfauzan.algotrack.data.datastore.DataStorePreference
import com.septalfauzan.algotrack.domain.model.UserData
import com.septalfauzan.algotrack.domain.model.apiResponse.RegisterResponse
import com.septalfauzan.algotrack.data.source.remote.apiInterfaces.AlgoTrackApiInterfaces
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class MainRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val apiServices: AlgoTrackApiInterfaces,
    private val dataStorePreference: DataStorePreference
){
    suspend fun registerUserRawJson(userData: UserData): RegisterResponse {
        val gson = Gson()
        val userDataJson = gson.toJson(userData)
        val requestBody = userDataJson.toRequestBody("application/json".toMediaTypeOrNull())
        return apiServices.register(requestBody)
    }
}