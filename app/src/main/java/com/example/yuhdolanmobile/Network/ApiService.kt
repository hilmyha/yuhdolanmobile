package com.example.yuhdolanmobile.Network

import com.example.yuhdolanmobile.Response.CategoryResponse
import com.example.yuhdolanmobile.Response.DestinasiResponse
import com.example.yuhdolanmobile.Response.LoginRequest
import com.example.yuhdolanmobile.Response.LoginResponse
import com.example.yuhdolanmobile.Response.LogoutResponse
import com.example.yuhdolanmobile.Response.UserResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {

    @Headers("Accept: application/json")
    @POST("login")
    fun loginUser(
        @Body request: LoginRequest
    ): Call<LoginResponse>

    @Headers("Accept: application/json")
    @POST("logout")
    fun logoutUser(): Call<LogoutResponse>

    @Headers("Accept: application/json")
    @GET("user")
    fun getUser(): Call<UserResponse>

    @GET("category")
    fun getCategory(): Call<CategoryResponse>

    @GET("destinasi")
    fun getDestinasi(): Call<DestinasiResponse>
}
