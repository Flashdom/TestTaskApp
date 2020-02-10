package com.example.testtaskapp.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.testtaskapp.R
import kotlinx.android.synthetic.main.activity_authorization.*


class AuthorizationActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authorization)
        if (getSharedPreferences("Settings", Context.MODE_PRIVATE).getString("accessToken", "") == "") {
            authme.setOnClickListener {
                val intent = Intent(this@AuthorizationActivity, WebAuthActivity::class.java)
                startActivity(intent)
            }
        } else {
            val intent = Intent(this@AuthorizationActivity, MainActivity::class.java)
            startActivity(intent)

        }
    }

}
