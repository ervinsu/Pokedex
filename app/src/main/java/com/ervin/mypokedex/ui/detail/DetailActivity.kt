package com.ervin.mypokedex.ui.detail

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.transition.Fade
import android.util.Log
import android.view.Window
import android.view.WindowManager
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

    private val job = Job()

    @SuppressLint("DefaultLocale")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            supportStartPostponedEnterTransition()
            val fade= Fade()
            setSupportActionBar(detail_toolbar)
            fade.excludeTarget(iv_poke_picture,true)
            fade.excludeTarget(poke_container,true)
            fade.excludeTarget(detail_toolbar,true)
            fade.excludeTarget(bg_detail,true)
            window.enterTransition = fade
            window.exitTransition = fade
        }
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
                    .into(iv_poke_picture)


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    val window: Window = window
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                    window.statusBarColor = Color.parseColor(pokemonModel.listTypeElementPokemon[0].typeEntity.typeColor)
                }
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
                Log.d("detailactv",pokemonModel.pokemon.desc.toString())
                supportActionBar?.setBackgroundDrawable(gd)
                supportActionBar?.title = pokemonModel.pokemon.pokemonName.capitalize()
                tv_poke_hp.text = pokemonModel.pokemon.hp.toString()
                progressbar_hp.progress = pokemonModel.pokemon.hp
                tv_poke_attack.text = pokemonModel.pokemon.attack.toString()
                progressbar_attack.progress = pokemonModel.pokemon.attack
                tv_type_name1.text = pokemonModel.listTypeElementPokemon[0].typeEntity.typeName
                tv_type_name1.setBackgroundColor(Color.parseColor(pokemonModel.listTypeElementPokemon[0].typeEntity.typeColor))
            }
        }
    }
}
