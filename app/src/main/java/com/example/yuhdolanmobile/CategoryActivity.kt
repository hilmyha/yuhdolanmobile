package com.example.yuhdolanmobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.core.app.NotificationCompat.getCategory
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.yuhdolanmobile.Adapter.CategoryAdapter
import com.example.yuhdolanmobile.Network.ApiClient
import com.example.yuhdolanmobile.Response.Category
import com.example.yuhdolanmobile.Response.CategoryResponse
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import retrofit2.Callback

class CategoryActivity : AppCompatActivity() {

    private lateinit var rvCategory: RecyclerView

    private lateinit var categoryAdapter: CategoryAdapter

    private lateinit var ivBack: ImageView

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private lateinit var skeleton: Skeleton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        categoryAdapter = CategoryAdapter(this@CategoryActivity, arrayListOf())

        rvCategory = findViewById(R.id.rv_category)
        rvCategory.layoutManager = GridLayoutManager(this@CategoryActivity, 3)
        rvCategory.adapter = categoryAdapter
        rvCategory.setHasFixedSize(true)

        skeleton = findViewById(R.id.skeletonLayout)
        skeleton = rvCategory.applySkeleton(R.layout.item_category_card, 3)

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)

        ivBack = findViewById(R.id.iv_back)
        ivBack.setOnClickListener {
            Intent(this@CategoryActivity, MainActivity::class.java).also {
                startActivity(it)
            }
        }

        swipeRefreshLayout.setOnRefreshListener {
            getRemoteCategory()
            swipeRefreshLayout.isRefreshing = false
        }

        skeleton.showSkeleton()
        getRemoteCategory()
    }

    private fun getRemoteCategory() {
        val handler = android.os.Handler()
        handler.postDelayed({
            ApiClient.apiService.getCategory().enqueue(object : Callback<CategoryResponse> {
                override fun onResponse(
                    call: retrofit2.Call<CategoryResponse>,
                    response: retrofit2.Response<CategoryResponse>
                ) {
                    if (response.isSuccessful) {
                        val data = response.body()?.data
                        if (data != null) {
                            setData(data)
                        }
                        swipeRefreshLayout.isRefreshing = false
                        skeleton.showOriginal()
                    }
                }

                override fun onFailure(call: retrofit2.Call<CategoryResponse>, t: Throwable) {
                    swipeRefreshLayout.isRefreshing = false
                    skeleton.showOriginal()
                }
            })
        }, 2000)
    }

    private fun setData(data: List<Category>) {
        categoryAdapter.setData(data)
    }
}