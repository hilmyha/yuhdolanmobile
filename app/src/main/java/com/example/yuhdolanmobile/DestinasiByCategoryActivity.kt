package com.example.yuhdolanmobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.yuhdolanmobile.Adapter.GridDestinasiAdapter
import com.example.yuhdolanmobile.Network.ApiClient
import com.example.yuhdolanmobile.Response.CategoryByIdResponse
import com.example.yuhdolanmobile.Response.Destinasi
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import com.faltenreich.skeletonlayout.createSkeleton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DestinasiByCategoryActivity : AppCompatActivity() {

    private lateinit var rvDestinasi: RecyclerView

    private lateinit var tvCategoryName: TextView
    private lateinit var destinasiAdapter: GridDestinasiAdapter

    private lateinit var ivBack: ImageView

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private lateinit var skeleton: Skeleton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_destinasi_by_category)

        tvCategoryName = findViewById(R.id.tv_title_category_card)
        tvCategoryName.text = intent.getStringExtra("title")

        val categoryId = intent.getStringExtra("category")?.toInt()

        destinasiAdapter = GridDestinasiAdapter(this@DestinasiByCategoryActivity, arrayListOf())

        rvDestinasi = findViewById(R.id.rv_destinasi_by_category)
        rvDestinasi.layoutManager = GridLayoutManager(this@DestinasiByCategoryActivity, 1)
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
        getDestinasiByCategory(categoryId)

        swipeRefreshLayout.setOnRefreshListener {
            getDestinasiByCategory(categoryId)
            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun getDestinasiByCategory(categoryId: Int?) {
        val handler = android.os.Handler()
        handler.postDelayed({
            swipeRefreshLayout.isRefreshing = true
            if (categoryId != null) {
//            Toast.makeText(this, categoryId.toString(), Toast.LENGTH_SHORT).show()
                ApiClient.apiService.getCategoryById(categoryId).enqueue(object : Callback<CategoryByIdResponse> {
                    override fun onResponse(
                        call: Call<CategoryByIdResponse>,
                        response: Response<CategoryByIdResponse>
                    ) {
                        if (response.isSuccessful) {
                            val data = response.body()?.destinasi
                            if (data != null) {
                                setDataToAdapter(data)
                            }
                            swipeRefreshLayout.isRefreshing = false
                            skeleton.showOriginal()
                        } else {
                            swipeRefreshLayout.isRefreshing = false
                            Toast.makeText(this@DestinasiByCategoryActivity, "Gagal", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<CategoryByIdResponse>, t: Throwable) {
                        swipeRefreshLayout.isRefreshing = false
                        skeleton.showOriginal()
                        Toast.makeText(this@DestinasiByCategoryActivity, t.toString(), Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }, 1000)

    }

    private fun setDataToAdapter(data: List<Destinasi>) {
        destinasiAdapter.setData(data)
    }
}