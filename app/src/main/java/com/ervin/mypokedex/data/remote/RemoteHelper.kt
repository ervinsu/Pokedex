package com.ervin.mypokedex.data.remote

import com.ervin.mypokedex.data.remote.entity.ListAPIPokemonResponse
import com.ervin.mypokedex.data.remote.entity.PokemonResponse
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


class RemoteHelper{

    private val service by lazy {
        Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build().create(PokemonFetchService::class.java)
    }
    suspend fun getDetailPokemon(id: String): PokemonResponse {
        return service.getPokemonDetail(id)
    }

    suspend fun getPokemons(offset:Int, limit:Int):ListAPIPokemonResponse{
        return service.getPokemons(offset, limit)
    }

    suspend fun getCountPokemon():String{
        return service.getCountPokemon()
    }

    suspend fun getPokemonTypes():ListAPIPokemonResponse{
        return service.getTypes()
    }
}