package com.example.testtaskapp.api.Models

data class PhotoModel(val id: Int, val album_id: Int, val owner_id: Int, val user_id:Int, val text : String, val date : Int, val sizes : Array<PhotoSizesModel>, val width : Int, val height:Int)