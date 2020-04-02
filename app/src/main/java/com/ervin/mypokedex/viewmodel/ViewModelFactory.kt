package com.ervin.mypokedex.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ervin.mypokedex.data.PokemonRepository
import com.ervin.mypokedex.di.Injection
import com.ervin.mypokedex.ui.detail.DetailViewModel
import com.ervin.mypokedex.ui.home.ui.aboutme.AboutMeViewModel
import com.ervin.mypokedex.ui.home.ui.quizpokemon.QuizPokemonViewModel
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
        when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                return MainViewModel(pokemonRepository) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                return DetailViewModel(pokemonRepository) as T
            }
            modelClass.isAssignableFrom(QuizPokemonViewModel::class.java) -> {
                return QuizPokemonViewModel(pokemonRepository) as T
            }
            modelClass.isAssignableFrom(AboutMeViewModel::class.java) ->{
                return AboutMeViewModel() as T
            }
            else -> return super.create(modelClass)
        }
    }
}