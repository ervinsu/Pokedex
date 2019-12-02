package com.ervin.mypokedex.data.remote


import com.ervin.mypokedex.data.remote.entity.ListAPIPokemonResponse
import com.ervin.mypokedex.data.remote.entity.PokemonResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonFetchService {

    @GET("pokemon/{id}/")
    suspend fun getPokemonDetail(@Path("id") pokemonID :String) : PokemonResponse

    @GET("pokemon/")
    suspend fun getPokemons(@Query("offset") listOffset : Int,
                            @Query("limit") listLimit : Int) : ListAPIPokemonResponse

    @GET("pokemon/")
    suspend fun getCountPokemon() : String

    @GET("type/")
    suspend fun getTypes() : ListAPIPokemonResponse
}