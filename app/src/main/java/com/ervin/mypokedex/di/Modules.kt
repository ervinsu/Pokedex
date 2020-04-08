package com.ervin.mypokedex.di

import com.ervin.mypokedex.data.PokemonRepository
import com.ervin.mypokedex.data.local.LocalRepository
import com.ervin.mypokedex.data.local.PokemonDatabase
import com.ervin.mypokedex.data.remote.RemoteHelper
import com.ervin.mypokedex.data.remote.RemoteRepository
import com.ervin.mypokedex.ui.detail.DetailViewModel
import com.ervin.mypokedex.ui.home.ui.aboutme.AboutMeViewModel
import com.ervin.mypokedex.ui.home.ui.quizpokemon.QuizPokemonViewModel
import com.ervin.mypokedex.ui.main.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    //initialize dao
    single { PokemonDatabase.getDatabase(androidContext()).dao() }

    //initialize local repo
    single { LocalRepository.getInstance(get()) }

    //initialize remote repo
    single { RemoteRepository.getInstance(RemoteHelper()) }

    //initialize Pokemon repo
    single { PokemonRepository.getInstance(get() , get(), androidContext()) }
}

val mainViewModelModule = module {
    viewModel {
        MainViewModel(get())
    }
}

val detailViewModelModule = module {
    viewModel {
        DetailViewModel(get())
    }
}

val quizPokemonViewModelModule = module {
    viewModel {
        QuizPokemonViewModel(get())
    }
}

val aboutMePokemonviewModelModule = module {
    viewModel {
        AboutMeViewModel()
    }
}