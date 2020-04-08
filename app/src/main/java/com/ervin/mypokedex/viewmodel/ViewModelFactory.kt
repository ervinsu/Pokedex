package com.ervin.mypokedex.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ervin.mypokedex.data.PokemonRepository
import com.ervin.mypokedex.ui.detail.DetailViewModel
import com.ervin.mypokedex.ui.home.ui.aboutme.AboutMeViewModel
import com.ervin.mypokedex.ui.home.ui.quizpokemon.QuizPokemonViewModel
import com.ervin.mypokedex.ui.main.MainViewModel
import org.koin.java.KoinJavaComponent.get

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(repository: PokemonRepository): ViewModelProvider.NewInstanceFactory() {

    private val pokemonRepository = repository

    companion object{
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(): ViewModelFactory {
            return INSTANCE ?: synchronized(ViewModelFactory::class.java){
                val instance = ViewModelFactory(get(PokemonRepository::class.java))
                INSTANCE = instance
                instance
            }
        }
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(pokemonRepository) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(pokemonRepository) as T
            }
            modelClass.isAssignableFrom(QuizPokemonViewModel::class.java) -> {
                QuizPokemonViewModel(pokemonRepository) as T
            }
            modelClass.isAssignableFrom(AboutMeViewModel::class.java) ->{
                AboutMeViewModel() as T
            }
            else -> super.create(modelClass)
        }
    }
}