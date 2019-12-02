package com.ervin.mypokedex.data.remote.entity

import com.google.gson.annotations.SerializedName

data class ListResponse (

    @SerializedName("name")
    var nameResponse: String,
    @SerializedName("url")
    var urlResponse: String
)