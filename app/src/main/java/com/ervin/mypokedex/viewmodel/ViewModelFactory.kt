package com.ervin.mypokedex.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ervin.mypokedex.data.PokemonRepository
import com.ervin.mypokedex.di.Injection
import com.ervin.mypokedex.ui.main.MainViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(repository: PokemonRepository): ViewModelProvider.NewInstanceFactory() {

    private val pokemonRepository = repository

    companion object{
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(application: Application): ViewModelFactory {
            return INSTANCE ?: synchronized(ViewModelFactory::class.java){
                val instance = ViewModelFactory(Injection.provideRepository(application))
                INSTANCE = instance
                instance
            }
        }
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MainViewModel::class.java)){
            return MainViewModel(pokemonRepository) as T
        }
        return super.create(modelClass)
    }
}