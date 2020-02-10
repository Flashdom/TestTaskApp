package com.example.testtaskapp.api

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.testtaskapp.api.Models.*
import com.google.gson.JsonObject
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


object DataRepo {
    private val apiService: Api = Uploader.create()


    fun uploadPhoto(
        token: String,
        file: File,
        user_id: Long,
        latitude: Double?,
        longitude: Double?
    ): LiveData<ResultModel> {

        val data: MutableLiveData<ResultModel> = MutableLiveData<ResultModel>()

        apiService.getWallUploadServer(token, "5.103")
            .enqueue(object : Callback<WallUploadServerModel> {
                override fun onFailure(call: Call<WallUploadServerModel>, t: Throwable) {
                    Log.d("ab", "c")
                }

                override fun onResponse(
                    call: Call<WallUploadServerModel>,
                    response: Response<WallUploadServerModel>
                ) {
                    val wallUploadServerModel = response.body()

                    val requestFile: RequestBody =
                        RequestBody.create(MediaType.parse("multipart/form-data"), file)
                    val multipartBody =
                        MultipartBody.Part.createFormData("photo", file.name, requestFile)
                    val uploader: Api = Uploader.createUploader(
                        wallUploadServerModel!!.response.upload_url.substring(
                            0,
                            34
                        ).replace("%", "?")
                    )
                    val tokenRequestBody: RequestBody =
                        RequestBody.create(MediaType.parse("multipart/form-data"), token)
                    val version: RequestBody =
                        RequestBody.create(MediaType.parse("multipart/form-data"), "5.103")
                    uploader.uploadPhoto(
                        wallUploadServerModel.response.upload_url.substring(44).replace(
                            "%3F",
                            "?"
                        ), multipartBody, tokenRequestBody, version
                    )
                        .enqueue(object : Callback<UploadPhotoModel> {
                            override fun onFailure(call: Call<UploadPhotoModel>, t: Throwable) {
                                Log.d("ab", "c")
                            }

                            override fun onResponse(
                                call: Call<UploadPhotoModel>,
                                response: Response<UploadPhotoModel>
                            ) {
                                val uploadModel = response.body()
                                apiService.saveWallPhoto(
                                    user_id,
                                    uploadModel!!.photo,
                                    uploadModel.server,
                                    uploadModel.hash,
                                    latitude,
                                    longitude,
                                    token,
                                    "5.103"
                                ).enqueue(
                                    object : Callback<Array<PhotoModel>> {
                                        override fun onFailure(
                                            call: Call<Array<PhotoModel>>,
                                            t: Throwable
                                        ) {
                                            Log.d("ab", "c")
                                        }

                                        override fun onResponse(
                                            call: Call<Array<PhotoModel>>,
                                            response: Response<Array<PhotoModel>>
                                        ) {
                                            val rez = response.body()
                                            val attachment = "photo" + user_id + "_" + rez!![0].id
                                            apiService.postOnWall(
                                                user_id,
                                                attachment,
                                                latitude,
                                                longitude,
                                                token,
                                                "5.103"
                                            ).enqueue(
                                                object : Callback<JsonObject> {
                                                    override fun onFailure(
                                                        call: Call<JsonObject>,
                                                        t: Throwable
                                                    ) {
                                                        Log.d("ab", "c")
                                                    }

                                                    override fun onResponse(
                                                        call: Call<JsonObject>,
                                                        response: Response<JsonObject>
                                                    ) {
                                                        if (response.code() == 200) {
                                                            val resultModel = ResultModel(true)
                                                            data.postValue(resultModel)
                                                        } else {
                                                            val resultModel = ResultModel(false)
                                                            data.postValue(resultModel)

                                                        }
                                                    }

                                                })
                                        }

                                    })
                            }

                        })
                }
            })
        return data
    }


    fun uploadAudioDoc(token: String,
                       file: File,
                       user_id: Long,
                       latitude: Double?,
                       longitude: Double?) : LiveData<ResultModel>
    {
        val data = MutableLiveData<ResultModel>()

        apiService.getWallUploadserverforDoc(token, "5.103").enqueue(object : Callback<WallUploadServerModel> {
            override fun onFailure(call: Call<WallUploadServerModel>, t: Throwable) {
                Log.d("ab", "c")
            }

            override fun onResponse(
                call: Call<WallUploadServerModel>,
                response: Response<WallUploadServerModel>
            ) {
                val wallUploadServerModel = response.body()

                val requestFile: RequestBody =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file)
                val multipartBody =
                    MultipartBody.Part.createFormData("file", file.name, requestFile)
                val uploader: Api = Uploader.createUploader(
                    wallUploadServerModel!!.response.upload_url.substring(
                        0,
                        34
                    ).replace("%", "?")
                )
                val tokenRequestBody: RequestBody =
                    RequestBody.create(MediaType.parse("multipart/form-data"), token)
                val version: RequestBody =
                    RequestBody.create(MediaType.parse("multipart/form-data"), "5.103")
                uploader.uploadPhoto(
                    wallUploadServerModel.response.upload_url.substring(44).replace(
                        "%3F",
                        "?"
                    ), multipartBody, tokenRequestBody, version)
                apiService.uploadDoc(  wallUploadServerModel.response.upload_url.substring(44).replace(
                    "%3F",
                    "?"
                ), multipartBody, tokenRequestBody, version).enqueue(object : Callback<UploadDocModel> {
                    override fun onFailure(call: Call<UploadDocModel>, t: Throwable) {
                        Log.d("a", "b")
                    }

                    override fun onResponse(
                        call: Call<UploadDocModel>,
                        response: Response<UploadDocModel>
                    ) {
                        val uploadDocModel = response.body()

                        apiService.saveDoc(uploadDocModel!!.file).enqueue(object : Callback<Array<ObjectModel>>{
                            override fun onFailure(call: Call<Array<ObjectModel>>, t: Throwable) {
                                Log.d("a", "b")
                            }

                            override fun onResponse(
                                call: Call<Array<ObjectModel>>,
                                response: Response<Array<ObjectModel>>
                            ) {
                                val attachment = "doc" + user_id + "_" + response.body()!![0].doc.id
                              apiService.postOnWall(user_id, attachment, latitude, longitude, token, "5.103").enqueue(object : Callback<JsonObject> {
                                  override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                                      Log.d("a", "b")
                                  }

                                  override fun onResponse(
                                      call: Call<JsonObject>,
                                      response: Response<JsonObject>
                                  ) {
                                      if (response.code() == 200) {
                                          val resultModel = ResultModel(true)
                                          data.postValue(resultModel)
                                      } else {
                                          val resultModel = ResultModel(false)
                                          data.postValue(resultModel)

                                      }

                                  }

                              })
                            }

                        })
                    }

                })


            }

        })
        return data

    }


}
