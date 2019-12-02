package com.ervin.mypokedex.di

import com.ervin.mypokedex.App
import com.ervin.mypokedex.MainActivity
import dagger.Component

@Component(modules = [RetrofitClientModule::class])
interface ServicePokemonComponent {
    fun injectMainActivity(app: MainActivity)
}