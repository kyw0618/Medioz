package com.iium.iium_medioz.util.network

import com.iium.iium_medioz.util.`object`.Constant
import com.iium.iium_medioz.util.`object`.Constant.BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RestUrlFlavor {

    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(Constant.DEFATULT_TIMEOUT.toLong(), TimeUnit.MILLISECONDS)
        .readTimeout(Constant.DEFATULT_TIMEOUT.toLong(), TimeUnit.MILLISECONDS)
        .writeTimeout(Constant.DEFATULT_TIMEOUT.toLong(), TimeUnit.MILLISECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}