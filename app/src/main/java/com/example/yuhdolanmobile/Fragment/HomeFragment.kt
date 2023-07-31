package com.example.yuhdolanmobile.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.yuhdolanmobile.Adapter.CategoryAdapter
import com.example.yuhdolanmobile.Adapter.DestinasiAdapter
import com.example.yuhdolanmobile.CategoryActivity
import com.example.yuhdolanmobile.DestinasiActivity
import com.example.yuhdolanmobile.Network.ApiClient
import com.example.yuhdolanmobile.R
import com.example.yuhdolanmobile.Response.Category
import com.example.yuhdolanmobile.Response.CategoryResponse
import com.example.yuhdolanmobile.Response.Destinasi
import com.example.yuhdolanmobile.Response.DestinasiResponse
import com.example.yuhdolanmobile.databinding.ActivityMainBinding
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import com.faltenreich.skeletonlayout.createSkeleton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var swipeRefresh: SwipeRefreshLayout

    private lateinit var rvCategory: RecyclerView
    private lateinit var categoryAdapter: CategoryAdapter

    private lateinit var rvDestinasi: RecyclerView
    private lateinit var destinasiAdapter: DestinasiAdapter

    private lateinit var categorySkeleton: Skeleton
    private lateinit var destinasiSkeleton: Skeleton

    private lateinit var tvCategoryAll: TextView
    private lateinit var tvDestinasiAll: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipeRefresh = view.findViewById(R.id.swipeRefreshLayout)
        rvCategory = view.findViewById(R.id.rv_category)
        rvDestinasi = view.findViewById(R.id.rv_destinasi)

        rvCategory.layoutManager = GridLayoutManager(context, 3)
        categoryAdapter = CategoryAdapter(dataList = arrayListOf(), context = requireContext())
        rvCategory.adapter = categoryAdapter

        rvDestinasi.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        destinasiAdapter = DestinasiAdapter(dataList = arrayListOf(), context = requireContext())
        rvDestinasi.adapter = destinasiAdapter

        categorySkeleton = view.findViewById(R.id.skeletonCategoryLayout)
        categorySkeleton = rvCategory.applySkeleton(R.layout.item_category_card, 6)
        destinasiSkeleton = view.findViewById(R.id.skeletonDestinasiLayout)
        destinasiSkeleton = rvDestinasi.applySkeleton(R.layout.item_destinasi_card, 6)

        tvCategoryAll = view.findViewById(R.id.show_category)
        tvCategoryAll.setOnClickListener(View.OnClickListener {
            val intent = Intent(context, CategoryActivity::class.java)
            startActivity(intent)
        })

        tvDestinasiAll = view.findViewById(R.id.show_destinasi)
        tvDestinasiAll.setOnClickListener(View.OnClickListener {
            val intent = Intent(context, DestinasiActivity::class.java)
            startActivity(intent)
        })

        categorySkeleton.showSkeleton()
        destinasiSkeleton.showSkeleton()
        getRemoteCategory()
        getRemoteDestinasi()

        swipeRefresh.setOnRefreshListener {
            getRemoteCategory()
        }
    }

    private fun getRemoteCategory() {
        val handler = android.os.Handler()
        handler.postDelayed({
            swipeRefresh.isRefreshing = true
            ApiClient.apiService.getCategory().enqueue(object : Callback<CategoryResponse> {
                override fun onResponse(
                    call: Call<CategoryResponse>,
                    response: Response<CategoryResponse>
                ) {
                    if (response.isSuccessful) {
                        val data = response.body()?.data
                        categoryAdapter.setData(data as ArrayList<Category>)
                    }
                    swipeRefresh.isRefreshing = false
                    categorySkeleton.showOriginal()
                }

                override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
                    swipeRefresh.isRefreshing = false
                    categorySkeleton.showOriginal()
                }
            })
        }, 1000)
    }

    private fun getRemoteDestinasi() {
        val handler = android.os.Handler()
        handler.postDelayed({
            swipeRefresh.isRefreshing = true
            ApiClient.apiService.getDestinasi().enqueue(object : Callback<DestinasiResponse> {
                override fun onResponse(
                    call: Call<DestinasiResponse>,
                    response: Response<DestinasiResponse>
                ) {
                    if (response.isSuccessful) {
                        val data = response.body()?.data
                        destinasiAdapter.setData(data as ArrayList<Destinasi>)
                    }
                    swipeRefresh.isRefreshing = false
                    destinasiSkeleton.showOriginal()
                }

                override fun onFailure(call: Call<DestinasiResponse>, t: Throwable) {
                    swipeRefresh.isRefreshing = false
                    destinasiSkeleton.showOriginal()
                }
            })
        }, 1000)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}