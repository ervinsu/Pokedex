package com.ervin.mypokedex.data.remote

import com.ervin.mypokedex.data.remote.entity.ListAPIPokemonResponse

class RemoteRepository(private val remoteHelper: RemoteHelper){
    companion object{
        private var INSTANCE :RemoteRepository? = null

        fun getInstance(remoteHelper: RemoteHelper) :RemoteRepository{
            return INSTANCE?: RemoteRepository(remoteHelper)
        }
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