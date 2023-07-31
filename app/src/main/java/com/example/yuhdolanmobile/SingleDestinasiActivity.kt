package com.example.yuhdolanmobile

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.yuhdolanmobile.Adapter.UlasanAdapter
import com.example.yuhdolanmobile.Network.ApiClient
import com.example.yuhdolanmobile.Response.DestinasiByIdResponse
import com.example.yuhdolanmobile.Response.DestinasiUlasanResponse
import com.faltenreich.skeletonlayout.Skeleton
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SingleDestinasiActivity : AppCompatActivity() {

    private lateinit var ivBack: ImageView

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private lateinit var skeleton: Skeleton

    private lateinit var rvUlasan: RecyclerView
    private lateinit var ulasanAdapter: UlasanAdapter

    private lateinit var tvNama: TextView
    private lateinit var tvHarga: TextView
    private lateinit var tvAlamat: TextView
    private lateinit var tvDeskripsi: TextView
    private lateinit var mapsView: SupportMapFragment

    private lateinit var btnTambahUlasan: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_destinasi)

        skeleton = findViewById(R.id.skeletonLayout)
        skeleton.showSkeleton()

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)

        val destinasiId = intent.getStringExtra("destinasi")?.toInt()
        val namaDestinasi = intent.getStringExtra("name")
        val excerptDestinasi = intent.getStringExtra("excerpt")

        tvHarga = findViewById(R.id.tv_price)
        tvNama = findViewById(R.id.tv_title)
        tvAlamat = findViewById(R.id.tv_location)
        tvDeskripsi = findViewById(R.id.tv_desc)

        ulasanAdapter = UlasanAdapter(this@SingleDestinasiActivity, arrayListOf())

        rvUlasan = findViewById(R.id.rv_ulasan)
        rvUlasan.layoutManager = GridLayoutManager(this@SingleDestinasiActivity, 1)
        rvUlasan.adapter = ulasanAdapter
        rvUlasan.setHasFixedSize(true)

        ivBack = findViewById(R.id.iv_back)
        ivBack.setOnClickListener {
            // go back to previous activity
            onBackPressed()
        }

        btnTambahUlasan = findViewById(R.id.btn_tambah_ulasan)
        btnTambahUlasan.setOnClickListener {
            Intent(this@SingleDestinasiActivity, TambahUlasanActivity::class.java).also {
                // with putExtra, we can send data to another activity
                val id = destinasiId.toString()
                it.putExtra("destinasi", id)
                it.putExtra("name", namaDestinasi)
                it.putExtra("excerpt", excerptDestinasi)
//                Toast.makeText(this, namaDestinasi, Toast.LENGTH_SHORT).show()
                startActivity(it)
            }
        }

        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
            getRemoteDestinasiSingle(destinasiId)
        }

        getRemoteDestinasiSingle(destinasiId)
        parallaxEffect()
    }

    private fun getRemoteDestinasiSingle(destinasiId: Int?) {
        val handler = android.os.Handler()
        handler.postDelayed({
            swipeRefreshLayout.isRefreshing = true
            if (destinasiId != null) {
                ApiClient.apiService.getDestinasiById(destinasiId).enqueue(object : Callback<DestinasiByIdResponse> {
                    @SuppressLint("SetTextI18n")
                    override fun onResponse(
                        call: Call<DestinasiByIdResponse>,
                        response: Response<DestinasiByIdResponse>
                    ) {
                        if (response.isSuccessful) {
                            tvNama.text = response.body()?.data?.nama
                            tvHarga.text = "IDR " + response.body()?.data?.harga.toString()
                            tvAlamat.text = response.body()?.data?.lokasi

                            // set maps
                            mapsView = supportFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
                            mapsView.getMapAsync { googleMap ->
                                val lat = response.body()?.data?.lat?.toDouble()
                                val lng = response.body()?.data?.lng?.toDouble()
                                val latLng = LatLng(lat!!, lng!!)
                                googleMap.addMarker(MarkerOptions().position(latLng).title(response.body()?.data?.nama))
                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13f))
                            }

                            // remove html tag from deskripsi
                            val html = response.body()?.data?.deskripsi
                            val spanned = Html.fromHtml(html)
                            tvDeskripsi.text = spanned.toString()

                            val ulasan = response.body()?.ulasan

                            if (ulasan != null) {
                                ulasanAdapter.setData(ulasan)
                            }

                            swipeRefreshLayout.isRefreshing = false
                            skeleton.showOriginal()
                        } else {
                            swipeRefreshLayout.isRefreshing = false
                            Toast.makeText(this@SingleDestinasiActivity, "Gagal", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<DestinasiByIdResponse>, t: Throwable) {
                        swipeRefreshLayout.isRefreshing = false
                        skeleton.showOriginal()
                        Toast.makeText(this@SingleDestinasiActivity, t.toString(), Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }, 2000)
    }



    fun parallaxEffect() {
        val parallaxImage = findViewById<ImageView>(R.id.parallax_image)

        val scrollView = findViewById<ScrollView>(R.id.scroll_view)
        scrollView.viewTreeObserver.addOnScrollChangedListener {
            val scrollY = scrollView.scrollY
            parallaxImage.translationY = scrollY.toFloat() / 2
        }
    }
}

