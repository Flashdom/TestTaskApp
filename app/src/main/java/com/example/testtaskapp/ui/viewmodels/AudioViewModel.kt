package com.example.testtaskapp.ui.viewmodels

import android.app.Application
import android.content.Context
import android.media.MediaRecorder
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.testtaskapp.api.DataRepo
import com.example.testtaskapp.api.Models.ResultModel
import ir.mahdi.mzip.zip.ZipArchive
import java.io.File


class AudioViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: DataRepo = DataRepo

    private val recorder = MediaRecorder()

    fun recordAudio() {
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC)
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        recorder.setOutputFile(getApplication<Application>().applicationContext.cacheDir.absolutePath)
        recorder.prepare()
        recorder.start()
    }

    fun stopRecording() {
        recorder.stop()
        recorder.release()
        ZipArchive.zip(
            getApplication<Application>().applicationContext.cacheDir.absolutePath,
            getApplication<Application>().applicationContext.cacheDir.absolutePath,
            ""
        )
    }


    fun uploadDocument(file: File, latitude: Double?, longitude: Double?) : LiveData<ResultModel>{

        val token: String = getApplication<Application>().applicationContext.getSharedPreferences(
            "Settings",
            Context.MODE_PRIVATE
        ).getString("accessToken", "").toString()
        val user_id: Long = getApplication<Application>().applicationContext.getSharedPreferences(
            "Settings",
            Context.MODE_PRIVATE
        ).getLong("user_id", 0)

        return repo.uploadAudioDoc(token,file, user_id, latitude, longitude)

    }


}
