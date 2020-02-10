package com.example.testtaskapp.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Uploader {

    fun createUploader(url:String): Api {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(url)
            .build()

        return retrofit.create(Api::class.java)
    }

        fun create(): Api {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.vk.com/method/")
                .build()

            return retrofit.create(Api::class.java)
        }

}