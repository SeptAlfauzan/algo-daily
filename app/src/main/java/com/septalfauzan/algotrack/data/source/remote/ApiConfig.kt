package com.septalfauzan.algotrack.data.source.remote

import com.septalfauzan.algotrack.data.source.remote.apiInterfaces.AlgoTrackApiInterfaces
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiConfig{
    companion object{
        private val logginInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        private val client = OkHttpClient.Builder()
            .addInterceptor(logginInterceptor)
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()

        private val retrofit = Retrofit.Builder().apply {
            baseUrl("https://algotrackapi-1-t4449346.deta.app/api/")
            addConverterFactory(GsonConverterFactory.create())
            client(client)
        }.build()

        fun getApiServices(): AlgoTrackApiInterfaces = retrofit.create(AlgoTrackApiInterfaces::class.java)
    }
}