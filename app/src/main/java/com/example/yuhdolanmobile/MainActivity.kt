package com.example.yuhdolanmobile

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.yuhdolanmobile.Fragment.FavoriteFragment
import com.example.yuhdolanmobile.Fragment.HomeFragment
import com.example.yuhdolanmobile.Fragment.ProfileFragment
import com.example.yuhdolanmobile.Fragment.UlasanFragment
import com.example.yuhdolanmobile.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var isLoggedIn = false
    private var isBackPressedOnce = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE)
        if (sharedPreferences.getString("Status", "Off") == "On") {
            isLoggedIn = true
        }

        replaceFragment(HomeFragment())

        binding.navbar.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.nav_home -> replaceFragment(HomeFragment())
                R.id.nav_favorite -> replaceFragment(FavoriteFragment())
                R.id.nav_ulasan -> replaceFragment(UlasanFragment())
                R.id.nav_profile -> replaceFragment(ProfileFragment())
            }
            true
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

    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_navbar, fragment)
        fragmentTransaction.commit()
    }
}