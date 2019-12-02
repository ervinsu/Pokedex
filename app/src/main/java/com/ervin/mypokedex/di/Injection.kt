package com.ervin.mypokedex.di

import android.annotation.SuppressLint
import android.app.Application
import com.ervin.mypokedex.data.PokemonRepository
import com.ervin.mypokedex.data.local.LocalRepository
import com.ervin.mypokedex.data.local.PokemonDatabase
import com.ervin.mypokedex.data.remote.RemoteHelper
import com.ervin.mypokedex.data.remote.RemoteRepository
import com.ervin.mypokedex.utils.AppExecutors

class Injection {

    companion object{
        @SuppressLint("VisibleForTests")
        fun provideRepository(application: Application): PokemonRepository {
            val app:AppExecutors = AppExecutors()
            val pokemonDao = PokemonDatabase.getDatabase(application).dao()
            val localRepository = LocalRepository.getInstance(pokemonDao)
            val remoteRepository = RemoteRepository.getInstance(RemoteHelper())
            return PokemonRepository.getInstance(remoteRepository,localRepository,app)
        }
    }

}