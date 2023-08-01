package com.example.yuhdolanmobile.Response

import com.google.gson.annotations.SerializedName
import org.json.JSONObject

data class UserResponse(
    @SerializedName("status") val status: String,
    @SerializedName("id") val id: Int,
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

data class DestinasiByIdResponse(
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: Destinasi,
    @SerializedName("ulasan") val ulasan: List<Ulasan>,
)

// ulasan
data class DestinasiUlasanResponse(
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: List<Ulasan>,
)

data class UlasanRequest(
    @SerializedName("judul") val judul: String,
    @SerializedName("isi") val isi: String,
    @SerializedName("tanggal_berkunjung") val tanggal_berkunjung: String,
)

data class UlasanResponse(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String,
)

data class Destinasi(
    @SerializedName("id") val id: Int,
    @SerializedName("nama") val nama: String,
    @SerializedName("lokasi") val lokasi: String,
    @SerializedName("excerpt") val excerpt: String,
    @SerializedName("deskripsi") val deskripsi: String,
    @SerializedName("harga") val harga: Int,
    @SerializedName("lat") val lat: String,
    @SerializedName("lng") val lng: String,
    @SerializedName("user_id") val user_id: Int,
    // nested category object
    @SerializedName("category") val category: Category,
)

data class Ulasan(
    @SerializedName("id") val id: Int,
    @SerializedName("destinasi_id") val destinasi_id: Int,
    @SerializedName("user_id") val user_id: Int,
    @SerializedName("judul") val judul: String,
    @SerializedName("isi") val isi: String,
    @SerializedName("tanggal_berkunjung") val tanggal_berkunjung: String,
    // nested ulasan object json
    @SerializedName("user") val user: User,
)

data class User(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String,
)