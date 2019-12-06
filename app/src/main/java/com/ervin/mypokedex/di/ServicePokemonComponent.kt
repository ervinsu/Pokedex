package com.ervin.mypokedex.di

import com.ervin.mypokedex.ui.main.MainActivity
import dagger.Component

@Component(modules = [RetrofitClientModule::class])
interface ServicePokemonComponent {
    fun injectMainActivity(app: MainActivity)
}