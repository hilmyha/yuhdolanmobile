package com.example.yuhdolanmobile

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.example.yuhdolanmobile.Network.ApiClient
import com.example.yuhdolanmobile.Response.UlasanRequest
import com.example.yuhdolanmobile.Response.UlasanResponse
import java.text.SimpleDateFormat
import java.util.Calendar

class TambahUlasanActivity : AppCompatActivity() {

    private lateinit var ivBack: ImageView

    private lateinit var btnTambahUlasan: Button
    private lateinit var btnTanggalBerkunjung: EditText

    private lateinit var tvDestinasiName: TextView
    private lateinit var tvDestinasiExcerpt: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_ulasan)

        ivBack = findViewById(R.id.iv_back)

        btnTanggalBerkunjung = findViewById(R.id.ulasan_tanggal_kunjungan)
        btnTambahUlasan = findViewById(R.id.btn_ulas)
        tvDestinasiName = findViewById(R.id.tv_destinasi_card_title)
        tvDestinasiExcerpt = findViewById(R.id.tv_excerpt)

        tvDestinasiName.text = intent.getStringExtra("name")
        tvDestinasiExcerpt.text = intent.getStringExtra("excerpt")

        btnTanggalBerkunjung.inputType = InputType.TYPE_NULL

        btnTanggalBerkunjung.setOnClickListener {
            showDatePickerDialog(btnTanggalBerkunjung)
        }

        btnTambahUlasan.setOnClickListener {
            // get destinasi id from intent
            val destinasiId = intent.getStringExtra("destinasi")
            val judul = findViewById<EditText>(R.id.ulasan_judul).text.toString()
            val isi = findViewById<EditText>(R.id.ulasan_isi).text.toString()
            val tanggalBerkunjung = findViewById<EditText>(R.id.ulasan_tanggal_kunjungan).text.toString()

            tambahUlasan(judul, isi)
        }

        ivBack.setOnClickListener {
            // go back to previous activity
            onBackPressed()
        }
    }

    private fun tambahUlasan(
        judul: String,
        isi: String,
    ) {
        val tanggalBerkunnjung = findViewById<EditText>(R.id.ulasan_tanggal_kunjungan).text.toString()
        val destinasiId = intent.getStringExtra("destinasi")!!.toInt()
        val ulasanRequest = UlasanRequest(judul, isi, tanggalBerkunnjung)
        val token = getSharedPreferences("Login", MODE_PRIVATE).getString("Token", null)

        val apiClient = ApiClient
        val ulasanCall = apiClient.create("Bearer $token").postUlasan(destinasiId, ulasanRequest)

        ulasanCall.enqueue(object : retrofit2.Callback<UlasanResponse> {
            override fun onResponse(
                call: retrofit2.Call<UlasanResponse>,
                response: retrofit2.Response<UlasanResponse>
            ) {
                if (response.isSuccessful) {
                    val ulasanResponse = response.body()
                    if (ulasanResponse != null) {
                        if (ulasanResponse.status == "success") {
                            onBackPressed()
                        }
                    }
                }
            }

            override fun onFailure(call: retrofit2.Call<UlasanResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })

    }

    @SuppressLint("SimpleDateFormat")
    private fun showDatePickerDialog(editText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDayOfMonth)
                val currentDate = Calendar.getInstance()

                if (editText == btnTanggalBerkunjung) {
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd")
                    val date = dateFormat.format(selectedDate.time)
                    editText.setText(date)
                }

            },
            year,
            month,
            dayOfMonth
        )

        datePickerDialog.show()
    }
}
