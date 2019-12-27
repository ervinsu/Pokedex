package com.ervin.mypokedex.data.remote


import com.ervin.mypokedex.data.remote.entity.ListAPIPokemonResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PokemonFetchService {


    @GET("pokemon/")
    suspend fun getPokemons(@Query("offset") listOffset : Int,
                            @Query("limit") listLimit : Int) : ListAPIPokemonResponse

    @GET("pokemon/")
    suspend fun getCountPokemon() : String

    @GET("type/")
    suspend fun getTypes() : ListAPIPokemonResponse
}