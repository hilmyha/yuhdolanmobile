package com.example.yuhdolanmobile.Response

import com.google.gson.annotations.SerializedName

data class ApiResponse(
    @SerializedName("status") val status: String,
    @SerializedName("data") val data: String
)

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

data class LogoutRequest(
    @SerializedName("token") val token: String
)

data class LogoutResponse(
    @SerializedName("status") val status: String
)
