package com.example.yuhdolanmobile.Network

import com.example.yuhdolanmobile.Response.CategoryByIdResponse
import com.example.yuhdolanmobile.Response.CategoryResponse
import com.example.yuhdolanmobile.Response.DestinasiByIdResponse
import com.example.yuhdolanmobile.Response.DestinasiResponse
import com.example.yuhdolanmobile.Response.LoginRequest
import com.example.yuhdolanmobile.Response.LoginResponse
import com.example.yuhdolanmobile.Response.LogoutResponse
import com.example.yuhdolanmobile.Response.UlasanRequest
import com.example.yuhdolanmobile.Response.UlasanResponse
import com.example.yuhdolanmobile.Response.UserResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

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

    @Headers("Accept: application/json")
    @GET("category/{id}")
    fun getCategoryById(
        @Path("id") categoryId: Int
    ): Call<CategoryByIdResponse>

    @GET("destinasi")
    fun getDestinasi(): Call<DestinasiResponse>

    @Headers("Accept: application/json")
    @GET("destinasi/{id}")
    fun getDestinasiById(
        @Path("id") destinasiId: Int
    ): Call<DestinasiByIdResponse>

    @Headers("Accept: application/json")
    @POST("ulasan/{destinasi}")
    fun postUlasan(
        @Path("destinasi") destinasiId: Int,
        @Body request: UlasanRequest
    ): Call<UlasanResponse>
}
