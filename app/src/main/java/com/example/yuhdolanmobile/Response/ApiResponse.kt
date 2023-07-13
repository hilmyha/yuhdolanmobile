package com.example.yuhdolanmobile.Response

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("status") val status: String,
    @SerializedName("name") val name: String,
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String,
)

data class LoginRequest(
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String
)

data class LoginResponse(
    @SerializedName("token") val token: String,
    @SerializedName("status") val status: String
)

data class LogoutResponse(
    @SerializedName("status") val status: String
)

// category: API
data class CategoryResponse(
    @SerializedName("status") val status: String,
    @SerializedName("data") val data: List<Category>,
    @SerializedName("destinasi") val destinasi: List<Destinasi>,
)

data class CategoryByIdResponse(
    @SerializedName("status") val status: String,
    @SerializedName("data") val data: Category,
    @SerializedName("destinasi") val destinasi: List<Destinasi>,
)

data class Category(
    @SerializedName("id") val id: Int,
    @SerializedName("nama") val nama: String,
)

// destinasi: API
data class DestinasiResponse(
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: List<Destinasi>,
)

data class Destinasi(
    @SerializedName("id") val id: Int,
    @SerializedName("nama") val nama: String,
    @SerializedName("lokasi") val lokasi: String,
    @SerializedName("deskripsi") val deskripsi: String,
    @SerializedName("user_id") val user_id: Int,
    // nested category object
    @SerializedName("category") val category: Category,
)
