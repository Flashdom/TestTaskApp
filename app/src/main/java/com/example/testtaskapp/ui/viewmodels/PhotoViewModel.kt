package com.example.testtaskapp.ui.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.testtaskapp.api.DataRepo
import com.example.testtaskapp.api.Models.ResultModel
import java.io.File

class PhotoViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: DataRepo = DataRepo

    fun uploadPhoto(file: File, latitude: Double?, longitude: Double?): LiveData<ResultModel> {
        val token: String = getApplication<Application>().applicationContext.getSharedPreferences(
            "Settings",
            Context.MODE_PRIVATE
        ).getString("accessToken", "").toString()
        val user_id: Long = getApplication<Application>().applicationContext.getSharedPreferences(
            "Settings",
            Context.MODE_PRIVATE
        ).getLong("user_id", 0)
        return repo.uploadPhoto(token, file, user_id, latitude, longitude)
    }


}
