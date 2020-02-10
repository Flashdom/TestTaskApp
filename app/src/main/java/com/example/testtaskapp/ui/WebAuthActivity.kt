package com.example.testtaskapp.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.testtaskapp.R
import kotlinx.android.synthetic.main.activity_web_auth.*


class WebAuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_auth)
        webView.loadUrl("https://oauth.vk.com/authorize?client_id=7311316&redirect_uri=https://oauth.vk.com/blank.html&scope=photos,wall,docs&response_type=token&v=5.103")
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String) {
                var url = url
                if (url.startsWith("https://oauth.vk.com/blank.html")) {
                    url = url.replace('#', '?')
                    val accessToken =
                        Uri.parse(url).getQueryParameter("access_token")
                    val userId =
                        java.lang.Long.valueOf(Uri.parse(url).getQueryParameter("user_id")!!)
                    if (accessToken != null) {
                        getSharedPreferences("Settings", Context.MODE_PRIVATE).edit()
                            .putString("accessToken", accessToken).putLong("userId", userId).apply()
                    }

                    val intent =
                        Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                }
            }
        }


    }
}

