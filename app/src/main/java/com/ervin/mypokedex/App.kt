package com.ervin.mypokedex

import android.app.Application
import android.content.Context
import com.ervin.mypokedex.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

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
        startKoin {
            androidContext(this@App)
            modules(listOf(appModule, mainViewModelModule, detailViewModelModule, quizPokemonViewModelModule, aboutMePokemonviewModelModule))
        }
    }

}