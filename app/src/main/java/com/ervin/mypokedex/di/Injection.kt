package com.ervin.mypokedex.di

import android.annotation.SuppressLint
import android.app.Application
import com.ervin.mypokedex.data.PokemonRepository
import org.koin.java.KoinJavaComponent.get

class Injection {

    companion object{
        @SuppressLint("VisibleForTests")
        fun provideRepository(application: Application): PokemonRepository {
//            val pokemonDao = PokemonDatabase.getDatabase(application).dao()
//            val localRepository = LocalRepository.getInstance(pokemonDao)
//            val remoteRepository = RemoteRepository.getInstance(RemoteHelper())
//            return PokemonRepository.getInstance(remoteRepository,localRepository)
            return get(PokemonRepository::class.java)
        }
    }

}