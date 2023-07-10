package com.example.yuhdolanmobile.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.yuhdolanmobile.Adapter.CategoryAdapter
import com.example.yuhdolanmobile.Network.ApiClient
import com.example.yuhdolanmobile.R
import com.example.yuhdolanmobile.Response.ApiResponse
import com.example.yuhdolanmobile.Response.Category
import com.example.yuhdolanmobile.Response.CategoryResponse
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

    private lateinit var skeleton: Skeleton

    private lateinit var binding: ActivityMainBinding

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

        rvCategory.layoutManager = GridLayoutManager(context, 3)
        categoryAdapter = CategoryAdapter(dataList = arrayListOf(), context = requireContext())
        rvCategory.adapter = categoryAdapter

        skeleton = view.findViewById(R.id.skeletonLayout)
        skeleton = rvCategory.applySkeleton(R.layout.item_category_card, 3)

        skeleton.showSkeleton()
        getRemoteCategory()

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
                    skeleton.showOriginal()
                }

                override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
                    swipeRefresh.isRefreshing = false
                    skeleton.showOriginal()
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