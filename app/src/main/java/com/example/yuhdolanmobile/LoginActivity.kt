package com.example.yuhdolanmobile

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.yuhdolanmobile.Network.ApiClient
import com.example.yuhdolanmobile.Response.LoginRequest
import com.example.yuhdolanmobile.Response.LoginResponse
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var inputUsername : TextInputEditText
    private lateinit var inputPassword : TextInputEditText
    private var isLoggedIn = false
    private var isBackPressedOnce = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val usernameEditText = findViewById<TextInputEditText>(R.id.username_login)
        val passwordEditText = findViewById<TextInputEditText>(R.id.password_login)

        val sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE)
        val ediSaveLogin = sharedPreferences.edit()

        if(sharedPreferences.getString("Status", "Off") == "On"){
            startActivity(Intent(this, MainActivity::class.java))
        }

        val btnLogin = findViewById<Button>(R.id.btn_login)

        btnLogin.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            loginUser(username, password)
        }
    }

    override fun onBackPressed() {
        if (isLoggedIn) {
            if (isBackPressedOnce) {
                // Close the application
                super.onBackPressed()
            } else {
                // Show a warning dialog
                showExitConfirmationDialog()
            }
        } else {
            super.onBackPressed()
        }
    }

    private fun showExitConfirmationDialog() {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setIcon(R.drawable.exit_confirm)
        alertDialog.setMessage("Anda yakin akan meninggalkan aplikasi?")
        alertDialog.setPositiveButton("Iya") { dialog: DialogInterface, _: Int ->
            isBackPressedOnce = true
            dialog.dismiss()
            finishAffinity()
        }
        alertDialog.setNegativeButton("Tidak") { dialog: DialogInterface, _: Int ->
            dialog.dismiss()
        }
        alertDialog.show()
    }

    private fun loginUser(username: String, password: String) {
        val loginRequest = LoginRequest(username, password)
        val loginCall = ApiClient.apiService.loginUser(loginRequest)
        loginCall.enqueue(object : Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if(response.isSuccessful){
                    val token = response.body()?.token

                    val sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("Status", "On")
                    editor.putString("Username", username)
                    editor.putString("Password", password)
                    editor.putString("Token", token)
                    editor.apply()
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                } else {
                    val messageError = response.errorBody()?.string()
                    Toast.makeText(this@LoginActivity, messageError, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(this@LoginActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun jumpToRegister(view: android.view.View) {
        val intent = Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://yuhdolan.my.id/register"));
        startActivity(intent);
    }
}