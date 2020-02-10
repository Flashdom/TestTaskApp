package com.example.testtaskapp.api.Models

data class Document(val id:Int, val owner_id:Int, val title : String, val size:Int, val ext : String, val url:String, val date:Int, val type :Int, val Preview : Any)
