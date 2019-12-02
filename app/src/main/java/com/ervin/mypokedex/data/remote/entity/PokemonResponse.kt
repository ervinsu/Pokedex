package com.ervin.mypokedex.data.remote.entity

import com.google.gson.annotations.SerializedName

data class PokemonResponse(
    @SerializedName("name")
    val pokemonNameResponse:String
)