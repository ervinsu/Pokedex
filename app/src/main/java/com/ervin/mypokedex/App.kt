package com.ervin.mypokedex

import android.app.Application
import android.content.Context
import com.ervin.mypokedex.data.remote.PokemonFetchService
import javax.inject.Inject

class App : Application() {
//    @Inject lateinit var pokemonFetchService: PokemonFetchService
//
//    init {
//        DaggerServicePokemonComponent.create().injectMainActivity(this)
//    }

    companion object{
        private lateinit var context: Context
    }

    fun getContext():Context{
        return context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

}