package com.ervin.mypokedex

import android.app.Application
import android.content.Context

class App : Application() {
//    @Inject lateinit var pokemonFetchService: PokemonFetchService
//
//    init {
//        DaggerServicePokemonComponent.create().injectMainActivity(this)
//    }

    companion object{
        private lateinit var context: Context
        private var totalAnswer = 5
        private var totalQuestion = 5
    }

    fun getContext():Context{
        return context
    }

    fun getTotalAnswer():Int{
        return totalAnswer
    }

    fun getTotalQuestion():Int{
        return totalQuestion
    }


    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

}