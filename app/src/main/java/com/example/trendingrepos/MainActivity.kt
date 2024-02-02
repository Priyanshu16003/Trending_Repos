package com.example.trendingrepos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import com.facebook.shimmer.ShimmerFrameLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val shimmer = findViewById<ShimmerFrameLayout>(R.id.shimmer_layout)
        shimmer.startShimmer()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return true
    }
}