package com.ervin.mypokedex.data.remote.entity

import com.google.gson.annotations.SerializedName

data class PokemonEntityResponse(
    @SerializedName("id")
    val pokemonId:String,
    @SerializedName("name")
    val pokemonName:String,
    @SerializedName("sprites")
    val pokemonSprite: PokemonSpritesResponse,
    @SerializedName("stats")
    val pokemonStats: List<PokemonStatsResponse>,
    @SerializedName("weight")
    val pokemonBaseWeight: Int,
    @SerializedName("base_experience")
    val pokemonBaseExp: Int,
    @SerializedName("height")
    val pokemonBaseHeight: Int
)

data class PokemonSpritesResponse(
    @SerializedName("back_default")
    val pokemonSpriteBack:String,
    @SerializedName("front_default")
    val pokemonSpriteFront:String
)

data class PokemonStatsResponse(
    @SerializedName("base_stat")
    val pokemonBaseStat: Int,
    @SerializedName("stat")
    val pokemonStat: PokemonStatResponse

)

data class PokemonStatResponse(
    @SerializedName("name")
    val statName:String,
    @SerializedName("url")
    val statUrl:String
)