package com.ervin.mypokedex.di

import com.ervin.mypokedex.data.remote.PokemonFetchService
import dagger.Module
import dagger.Provides
import me.linshen.retrofit2.adapter.LiveDataCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
class RetrofitClientModule {
    @Singleton
    @Provides
    fun provideGithubService(): PokemonFetchService {
        return Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .build()
            .create(PokemonFetchService::class.java)
    }
}