package com.example.yuhdolanmobile.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.yuhdolanmobile.Adapter.UlasanAdapter
import com.example.yuhdolanmobile.Adapter.UlasanFragmentAdapter
import com.example.yuhdolanmobile.Network.ApiClient
import com.example.yuhdolanmobile.R
import com.example.yuhdolanmobile.Response.DestinasiUlasanResponse
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
 * Use the [UlasanFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UlasanFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var rvUlasan: RecyclerView
    private lateinit var ulasanAdapter: UlasanFragmentAdapter

    private lateinit var swipeRefresh: SwipeRefreshLayout

    private lateinit var skeleton: Skeleton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ulasan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipeRefresh = view.findViewById(R.id.swipeRefreshLayout)
        rvUlasan = view.findViewById(R.id.rv_ulasan)

        rvUlasan.layoutManager = GridLayoutManager(context, 1)
        ulasanAdapter = UlasanFragmentAdapter(dataList = arrayListOf(), context = requireContext())
        rvUlasan.adapter = ulasanAdapter

        skeleton = view.findViewById(R.id.skeletonLayout)
        skeleton = rvUlasan.applySkeleton(R.layout.item_ulasan_card, 6)

        skeleton.showSkeleton()
        getUlasan()

        swipeRefresh.setOnRefreshListener {
            getUlasan()
            swipeRefresh.isRefreshing = false
        }
    }

    private fun getUlasan() {
        val handler = android.os.Handler()
        handler.postDelayed({
            // get username data from shared preferences
            val username = context?.getSharedPreferences("Login", 0)?.getString("Username", "null")

            ApiClient.apiService.getUlasan().enqueue(object : Callback<DestinasiUlasanResponse>{
                override fun onResponse(
                    call: Call<DestinasiUlasanResponse>,
                    response: Response<DestinasiUlasanResponse>
                ) {
                    if (response.isSuccessful) {
                        val data = response.body()?.data
                        // filter data by username
                        data?.filter { it.user.username == username }?.let { ulasanAdapter.setData(it) }
                        skeleton.showOriginal()
                    }
                }

                override fun onFailure(call: Call<DestinasiUlasanResponse>, t: Throwable) {
                    Log.e("UlasanFragment", "onFailure: ${t.message}")
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
         * @return A new instance of fragment UlasanFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UlasanFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}