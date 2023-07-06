package com.example.yuhdolanmobile.Fragment

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.yuhdolanmobile.LoginActivity
import com.example.yuhdolanmobile.Network.ApiClient
import com.example.yuhdolanmobile.R
import com.example.yuhdolanmobile.Response.LogoutResponse
import com.example.yuhdolanmobile.Response.UserResponse
import retrofit2.Call
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var btnLogout: Button
    private lateinit var tvName: TextView
    private lateinit var tvEmail: TextView

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
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view:View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressDialog()


        btnLogout = view.findViewById(R.id.btn_logout)
        tvName = view.findViewById(R.id.tv_name_profile)
        tvEmail = view.findViewById(R.id.tv_email_profile)

        // get user data from api
        getUserData()

        // logout from api and go to login activity
        btnLogout.setOnClickListener(View.OnClickListener {
            // confirm logout
            val builder = AlertDialog.Builder(requireActivity())
            builder.setTitle("Logout")
            builder.setMessage("Are you sure want to logout?")
            builder.setPositiveButton("Yes") { dialog: DialogInterface, _ ->
                dialog.dismiss()
//                logout()
                userLogout()
            }
            builder.setNegativeButton("No") { dialog: DialogInterface, _ ->
                dialog.dismiss()
            }
            builder.show()
        })
    }

    private fun userLogout() {
        val sharedPreferences = requireActivity().getSharedPreferences("Login", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("Token", "")

        val apiClient = ApiClient
        val apiCall = apiClient.create("Bearer $token")?.logoutUser()

        apiCall?.enqueue(object : retrofit2.Callback<LogoutResponse> {
            override fun onResponse(call: Call<LogoutResponse>, response: Response<LogoutResponse>) {
                if (response.isSuccessful) {
                    val editor = sharedPreferences.edit()
                    editor.clear()
                    editor.apply()

                    val intent = Intent(requireActivity(), LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    requireActivity().finish()
                } else {
                    Toast.makeText(requireActivity(), "Failed to logout", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LogoutResponse>, t: Throwable) {
                Toast.makeText(requireActivity(), "Failed to logout", Toast.LENGTH_SHORT).show()
                Log.e("Error", t.toString())
            }
        })
    }

    private fun progressDialog() {
        val progressDialog = ProgressDialog(requireActivity())
        progressDialog.setMessage("Mengambil data")
        progressDialog.show()
        // delay 2 seconds
        val handler = android.os.Handler()
        handler.postDelayed({
            progressDialog.dismiss()
        }, 1500)
    }
    private fun getUserData() {
        val sharedPreferences = requireActivity().getSharedPreferences("Login", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("Token", "")

        val apiClient = ApiClient
        val apiCall = apiClient.create("Bearer $token")?.getUser()

        apiCall?.enqueue(object : retrofit2.Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    tvName.text = response.body()?.name
                    tvEmail.text = response.body()?.email
                } else {
                    Toast.makeText(requireActivity(), "Failed to get user data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Toast.makeText(requireActivity(), "Failed to get user data", Toast.LENGTH_SHORT).show()
                Log.e("Error", t.toString())
            }
        })


    }

//    private fun logout() {
//        val sharedPreferences = requireActivity().getSharedPreferences("Login", Context.MODE_PRIVATE)
//        val editor = sharedPreferences.edit()
//        editor.clear()
//        editor.apply()
//
//        val intent = Intent(requireActivity(), LoginActivity::class.java)
//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        startActivity(intent)
//        requireActivity().finish()
//    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
