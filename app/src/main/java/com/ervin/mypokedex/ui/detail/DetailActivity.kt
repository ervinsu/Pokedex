package com.ervin.mypokedex.ui.detail

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.transition.Fade
import android.util.Log
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
    private val listDetailType : MutableList<DetailTypeAdapter.PokemonTypeWeaknessFrom> = arrayListOf()
    val map = mutableMapOf<String,DetailTypeAdapter.PokemonTypeWeaknessFrom>()

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
                    }

                    for (i in pokemonModel.listTypeElementPokemon.indices){
                        val type = pokemonModel.listTypeElementPokemon[i]

                        for (k in type.typeSuperEffectiveEntityFrom.indices){
                            val currType = type.typeSuperEffectiveEntityFrom[k]
                            Log.d("supereffectve", "${currType.typeName} ${type.typeEntity.typeName}")
                            if(map.containsKey(currType.typeName))
                                map[currType.typeName] = DetailTypeAdapter.PokemonTypeWeaknessFrom(
                                    (map[currType.typeName]!!.typeElementWeaknessMultiply) * 2f, currType.typeColor)
                            else
                                map[currType.typeName] = DetailTypeAdapter.PokemonTypeWeaknessFrom(2f, currType.typeColor)
                        }

                        for (k in type.typeNotSuperEffectiveEntityFrom.indices){
                            val currType = type.typeNotSuperEffectiveEntityFrom[k]
                            Log.d("noteffective", "${currType.typeName} ${type.typeEntity.typeName}")
                            if(map.containsKey(currType.typeName))
                                map[currType.typeName] = DetailTypeAdapter.PokemonTypeWeaknessFrom(
                                    (map[currType.typeName]!!.typeElementWeaknessMultiply) * 0.5f, currType.typeColor)
                            else
                                map[currType.typeName] = DetailTypeAdapter.PokemonTypeWeaknessFrom(0.5f, currType.typeColor)
                        }

                        for (k in type.typeNoDamageEntityFrom.indices){
                            val currType = type.typeNoDamageEntityFrom[k]
                            Log.d("no_dmg", "${currType.typeName} ${type.typeEntity.typeName}")
                            if(map.containsKey(currType.typeName))
                                map[currType.typeName] = DetailTypeAdapter.PokemonTypeWeaknessFrom(
                                    (map[currType.typeName]!!.typeElementWeaknessMultiply) * 0f, currType.typeColor)
                            else
                                map[currType.typeName] = DetailTypeAdapter.PokemonTypeWeaknessFrom(0f, currType.typeColor)
                        }
                    }
                    Log.d("before_sor ted", "${map.size} ")
                    val sortedMap = map.toSortedMap(compareByDescending<String>{map[it]?.typeElementWeaknessMultiply}.thenBy { map[it]?.typeColor })
                    Log.d("after_sorted", "${sortedMap.size} ")
                    listDetailType.addAll(convertMapKey(sortedMap))
                    detailTypeAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun convertMapKey(map : Map<String, DetailTypeAdapter.PokemonTypeWeaknessFrom>):List<DetailTypeAdapter.PokemonTypeWeaknessFrom>{
        val listDetailType: MutableList<DetailTypeAdapter.PokemonTypeWeaknessFrom> = arrayListOf()
        for ((key,value) in map){
            value.typeName = key
            listDetailType.add(value)
        }
//        val sortedList = listDetailType.sortedWith(compareByDescending {  })
        return listDetailType
    }
}
