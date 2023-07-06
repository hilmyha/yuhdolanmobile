package com.example.yuhdolanmobile.Network

import okhttp3.OkHttp
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "https://yuhdolan.my.id/api/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    // Add token to header
    fun create(token: String): ApiService {
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor { chain ->
            val original: Request = chain.request()
            val requestBuilder: Request.Builder = original.newBuilder().header("Authorization", "Bearer $token").method(original.method, original.body)
            val request: Request = requestBuilder.build()
            chain.proceed(request)
        }

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(ApiService::class.java)
    }
}