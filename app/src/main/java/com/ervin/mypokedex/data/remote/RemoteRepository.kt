package com.ervin.mypokedex.data.remote

import com.ervin.mypokedex.data.remote.entity.ListAPIPokemonResponse
import com.ervin.mypokedex.data.remote.entity.PokemonResponse

class RemoteRepository(private val remoteHelper: RemoteHelper){
    companion object{
        private var INSTANCE :RemoteRepository? = null

        fun getInstance(remoteHelper: RemoteHelper) :RemoteRepository{
            return INSTANCE?: RemoteRepository(remoteHelper)
        }
    }

    suspend fun loadSimplePokemon(id : String): PokemonResponse {
        return remoteHelper.getDetailPokemon(id)
    }

    suspend fun loadPokemons(offset:Int, limit:Int):ListAPIPokemonResponse{
        return remoteHelper.getPokemons(offset,limit)
    }

    suspend fun loadPokemonTypes():ListAPIPokemonResponse{
        return remoteHelper.getPokemonTypes()
    }

    suspend fun getCountPokemonRepo():String{
        return remoteHelper.getCountPokemon()
    }

}