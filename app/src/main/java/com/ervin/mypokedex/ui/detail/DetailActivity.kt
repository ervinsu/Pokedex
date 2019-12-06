package com.ervin.mypokedex.ui.detail

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.transition.Fade
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.ervin.mypokedex.R
import com.ervin.mypokedex.data.PokemonRepository
import com.ervin.mypokedex.data.local.LocalRepository
import com.ervin.mypokedex.data.local.PokemonDatabase
import com.ervin.mypokedex.data.remote.RemoteHelper
import com.ervin.mypokedex.data.remote.RemoteRepository
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.coroutines.*

class DetailActivity : AppCompatActivity() {

    val job = Job()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        supportStartPostponedEnterTransition()
        val fade= Fade()
        setSupportActionBar(detail_toolbar)
        fade.excludeTarget(poke_picture,true)
        fade.excludeTarget(poke_container,true)
        fade.excludeTarget(detail_toolbar,true)
        window.enterTransition = fade
        window.exitTransition = fade
        val pokeId = intent.getIntExtra("tes",0)
        CoroutineScope(job + Dispatchers.IO).launch {
            val pokemonDao = PokemonDatabase.getDatabase(application).dao()
            val localRepository = LocalRepository.getInstance(pokemonDao)
            val remoteRepository = RemoteRepository.getInstance(RemoteHelper())
            val pokerepo = PokemonRepository.getInstance(remoteRepository,localRepository)
            val pokemonModel = pokerepo.getSpecificPokemon(pokeId)
            withContext(Dispatchers.Main){
                Glide.with(this@DetailActivity)
                    .load(pokemonModel.pokemon.pokemonSpritesUrl)
                    .into(poke_picture)

                val arrayColorTypes = IntArray(2)
                for (i in pokemonModel.listTypeElementPokemon.indices) {
                    arrayColorTypes[i] = Color.parseColor(pokemonModel.listTypeElementPokemon[i].typeEntity.typeColor)
                    if (pokemonModel.listTypeElementPokemon.size == 1){
                        arrayColorTypes[1] = Color.parseColor(pokemonModel.listTypeElementPokemon[0].typeEntity.typeColor)
                    }
                }

                val gd = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, arrayColorTypes)
                gd.cornerRadius = 0f
                poke_container.background = gd
                supportActionBar?.setBackgroundDrawable(gd)
                supportActionBar?.title = "tesstgqsarsgqwadsaz"
            }
        }

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            pokePicture.transitionName = "pokePicture"
//            pokeContainer.transitionName = "pokeContainer"
//        }
    }
}
