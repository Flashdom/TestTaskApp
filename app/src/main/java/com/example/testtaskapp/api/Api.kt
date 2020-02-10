package com.example.testtaskapp.api


import com.example.testtaskapp.api.Models.*
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface Api {

    @FormUrlEncoded
    @POST("wall.post")
    fun postOnWall(
        @Field("owner_id") owner_id: Long, @Field("attachments") attachments: String, @Field("lat") lat: Double?, @Field(
            "long"
        ) long: Double?, @Field("access_token") token: String, @Field("v") v: String
    ): Call<JsonObject>


    @GET("photos.getWallUploadServer")
    fun getWallUploadServer(@Query("access_token") token: String, @Query("v") v: String): Call<WallUploadServerModel>


    @Multipart
    @POST("upload.php{subPath}")
    fun uploadPhoto(
        @Path("subPath") path: String,
        @Part file: MultipartBody.Part, @Part("access_token") token: RequestBody, @Part(
            "v"
        ) v: RequestBody
    ): Call<UploadPhotoModel>

    @FormUrlEncoded
    @POST("photos.saveWallPhoto")
    fun saveWallPhoto(
        @Field("user_id") user_id: Long, @Field("photo") photo: String, @Field("server") server: Int, @Field(
            "hash"
        ) hash: String, @Field("latitude") latitude: Double?, @Field("longtitude") longtitude: Double?, @Field(
            "access_token"
        ) token: String, @Field("v") v: String
    ): Call<Array<PhotoModel>>


    @GET("docs.getWallUploadServer")
    fun getWallUploadserverforDoc(@Query("access_token") token: String, @Query("v") v: String): Call<WallUploadServerModel>


    @Multipart
    @POST("upload.php{subPath}")
    fun uploadDoc(
        @Path("subPath") path: String,
        @Part file: MultipartBody.Part, @Part("access_token") token: RequestBody, @Part(
            "v"
        ) v: RequestBody
    ): Call<UploadDocModel>


    @FormUrlEncoded
    @POST("docs.save")
    fun saveDoc(@Field("file") file :String) : Call<Array<ObjectModel>>

}