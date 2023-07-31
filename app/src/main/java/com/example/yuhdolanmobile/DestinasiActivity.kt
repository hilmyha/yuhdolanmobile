package com.example.yuhdolanmobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.yuhdolanmobile.Adapter.GridDestinasiAdapter
import com.example.yuhdolanmobile.Network.ApiClient
import com.example.yuhdolanmobile.Response.DestinasiResponse
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import retrofit2.Call
import retrofit2.Response

class DestinasiActivity : AppCompatActivity() {

    private lateinit var rvDestinasi: RecyclerView

    private lateinit var destinasiAdapter: GridDestinasiAdapter

    private lateinit var ivBack: ImageView

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private lateinit var skeleton: Skeleton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_destinasi)

        destinasiAdapter = GridDestinasiAdapter(this@DestinasiActivity, arrayListOf())

        rvDestinasi = findViewById(R.id.rv_destinasi)
        rvDestinasi.layoutManager = GridLayoutManager(this@DestinasiActivity, 1)
        rvDestinasi.adapter = destinasiAdapter
        rvDestinasi.setHasFixedSize(true)

        skeleton = findViewById(R.id.skeletonLayout)
        skeleton = rvDestinasi.applySkeleton(R.layout.item_category_destinasi_card, 10)

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)

        ivBack = findViewById(R.id.iv_back)
        ivBack.setOnClickListener {
            // go back to previous activity
            onBackPressed()
        }

        skeleton.showSkeleton()
        getRemoteDestinasi()

        swipeRefreshLayout.setOnRefreshListener {
            getRemoteDestinasi()
            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun getRemoteDestinasi() {
        val handler = android.os.Handler()
        handler.postDelayed({
            swipeRefreshLayout.isRefreshing = true

            ApiClient.apiService.getDestinasi().enqueue(object : retrofit2.Callback<DestinasiResponse> {
                override fun onResponse(
                    call: Call<DestinasiResponse>,
                    response: Response<DestinasiResponse>
                ) {
                    if (response.isSuccessful) {
                        val data = response.body()?.data
                        if (data != null) {
                            destinasiAdapter.setData(data)
                        }
                    }
                    swipeRefreshLayout.isRefreshing = false
                    skeleton.showOriginal()
                }

                override fun onFailure(call: Call<DestinasiResponse>, t: Throwable) {
                    swipeRefreshLayout.isRefreshing = false
                    skeleton.showOriginal()
                }
            })
        }, 2000)
    }
}