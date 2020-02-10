package com.example.testtaskapp.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.testtaskapp.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction()
            .add(R.id.MainLayout, MainMenuFragment.newInstance(), "mainmenu").commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.optionsmenu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId)
        {
          R.id.logout -> {
              getSharedPreferences("Settings", Context.MODE_PRIVATE).edit().clear().apply()
              startActivity(Intent(this@MainActivity, AuthorizationActivity::class.java))
          }
        }
        return super.onOptionsItemSelected(item)
    }
}
