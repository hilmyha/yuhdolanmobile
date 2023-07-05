package com.example.yuhdolanmobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.yuhdolanmobile.Fragment.FavoriteFragment
import com.example.yuhdolanmobile.Fragment.HomeFragment
import com.example.yuhdolanmobile.Fragment.ProfileFragment
import com.example.yuhdolanmobile.Fragment.UlasanFragment
import com.example.yuhdolanmobile.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_navbar, fragment)
        fragmentTransaction.commit()
    }
}