package com.ervin.mypokedex.data.remote.entity

import com.google.gson.annotations.SerializedName

data class ListAPIPokemonResponse (
    @SerializedName("count")
    val total:Int,

    @SerializedName("next")
    val nextLink:String,

    @SerializedName("previous")
    val previousLink:String,

    @SerializedName("results")
    val listResponseAPI:List<ListResponse>
)