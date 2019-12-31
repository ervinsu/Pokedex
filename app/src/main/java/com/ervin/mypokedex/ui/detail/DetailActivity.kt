package com.ervin.mypokedex.ui.detail

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.transition.Fade
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.ervin.mypokedex.R
import com.ervin.mypokedex.databinding.ActivityDetailBinding
import com.ervin.mypokedex.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class DetailActivity : AppCompatActivity() {

    private val job = Job()
    private val listDetailType: MutableList<DetailTypeAdapter.PokemonTypeWeaknessFrom> = arrayListOf()
    private val detailViewModel: DetailViewModel by lazy {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this@DetailActivity.application)
        return@lazy ViewModelProvider(this@DetailActivity, factory).get(DetailViewModel::class.java)
    }

    private lateinit var detailTypeAdapter: DetailTypeAdapter

    companion object{
        const val INTENT_POKEMON_ID = "PokemonID"
    }

    @SuppressLint("DefaultLocale")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityDetailBinding = DataBindingUtil.setContentView(this@DetailActivity, R.layout.activity_detail)
        binding.lifecycleOwner = this@DetailActivity
        binding.viewModel = detailViewModel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            supportStartPostponedEnterTransition()
            val fade= Fade()
            setSupportActionBar(detail_toolbar)
            fade.excludeTarget(iv_poke_picture,true)
            fade.excludeTarget(poke_container,true)
            fade.excludeTarget(bg_detail,true)
            window.enterTransition = fade
            window.exitTransition = fade
        }
        detailTypeAdapter = DetailTypeAdapter(listDetailType)
        rv_list_detail_weakness.adapter = detailTypeAdapter

        val pokeId = intent.getIntExtra(INTENT_POKEMON_ID,0)

        detailViewModel.apply {
            CoroutineScope(job+Dispatchers.Main).launch{
                setPokemonId(pokeId).collect{ pokemonModel->
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        val window: Window = window
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                        window.statusBarColor = Color.parseColor(pokemonModel.listTypeElementPokemon[0].typeEntity.typeColor)
                        val map = mutableMapOf<String,String>()
                        for (i in pokemonModel.listTypeElementPokemon.indices){
                            val type = pokemonModel.listTypeElementPokemon[i]

                            for(j in type.typeSuperEffectiveEntity.indices){
                                val currType = type.typeSuperEffectiveEntity[j]
                                map[currType.typeName] = "a"
                                listDetailType.add(DetailTypeAdapter.PokemonTypeWeaknessFrom(0, currType.typeName, currType.typeColor))
                            }
                            for(j in type.typeNotSuperEffectiveEntity.indices){
                                val currType = type.typeNotSuperEffectiveEntity[j]
                                listDetailType.add(DetailTypeAdapter.PokemonTypeWeaknessFrom(1, currType.typeName, currType.typeColor))
                            }
                            for(j in type.typeNoDamageEntity.indices){
                                val currType = type.typeNoDamageEntity[j]
                                listDetailType.add(DetailTypeAdapter.PokemonTypeWeaknessFrom(2, currType.typeName, currType.typeColor))
                            }
                        }

                        detailTypeAdapter.notifyDataSetChanged()

                    }
                }
            }
        }
    }
}
