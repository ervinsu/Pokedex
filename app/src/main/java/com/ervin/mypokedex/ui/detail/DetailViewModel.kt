package com.ervin.mypokedex.ui.detail

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ervin.mypokedex.App
import com.ervin.mypokedex.R
import com.ervin.mypokedex.data.PokemonRepository
import com.ervin.mypokedex.data.model.PokemonModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DetailViewModel(private val pokemonRepository: PokemonRepository) :ViewModel(){

    var pokemonId = MutableLiveData<String>()
    val gradientElements = MutableLiveData<GradientDrawable>()
    val spriteUrl = MutableLiveData<String>("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/132.png")
    val pokemonName = MutableLiveData<String>()
    val pokemonHp = MutableLiveData<Int>()
    val pokemonAtk = MutableLiveData<Int>()
    val pokemonDef = MutableLiveData<Int>()
    val pokemonSpeed = MutableLiveData<Int>()
    val pokemonSpAtk = MutableLiveData<Int>()
    val pokemonSpDef = MutableLiveData<Int>()
    val pokemonProfile = MutableLiveData<String>()
    val pokemonWeight = MutableLiveData<String>()
    val pokemonHeight = MutableLiveData<String>()
    val gradientElement1 = MutableLiveData<GradientDrawable>()
    val gradientElement2 = MutableLiveData<GradientDrawable>()
    val pokemonElement1 = MutableLiveData<String>()
    val pokemonElement2 = MutableLiveData<String>()
    val isType2Available = MutableLiveData<Boolean>()

    @SuppressLint("DefaultLocale")
    fun setPokemonId(id:Int):Flow<PokemonModel> = flow{
        pokemonId.value = "#$id"
        val pokemonModel = pokemonRepository.getSpecificPokemon(id)
        val arrayColor = IntArray(2)
        for (i in pokemonModel.listTypeElementPokemon.indices) {
            arrayColor[i] = Color.parseColor(pokemonModel.listTypeElementPokemon[i].typeEntity.typeColor)

            when (i) {
                0 -> {
                    pokemonElement1.value =
                        pokemonModel.listTypeElementPokemon[0].typeEntity.typeName.capitalize()
                    val gradient = GradientDrawable().apply {
                        setColor(Color.parseColor(pokemonModel.listTypeElementPokemon[0].typeEntity.typeColor))
                        cornerRadius = 8f
                        setStroke(3, ContextCompat.getColor(App().getContext(), R.color.darkGrey))
                    }
                    gradientElement1.value = gradient
                    isType2Available.value = false
                    if(pokemonModel.listTypeElementPokemon.size == 1)
                        arrayColor[1] = Color.parseColor(pokemonModel.listTypeElementPokemon[0].typeEntity.typeColor)
                }
                else -> {
                    isType2Available.value = true
                    pokemonElement2.value = pokemonModel.listTypeElementPokemon[i].typeEntity.typeName.capitalize()
                    val gradient = GradientDrawable().apply {
                        setColor(Color.parseColor(pokemonModel.listTypeElementPokemon[i].typeEntity.typeColor))
                        cornerRadius = 8f
                        setStroke(3, ContextCompat.getColor(App().getContext(), R.color.darkGrey))
                    }
                    gradientElement2.value = gradient
                }
            }
        }
        gradientElements.value = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, arrayColor).apply {
            cornerRadius = 0f
        }
        spriteUrl.value = pokemonModel.pokemon.pokemonSpritesUrl
        pokemonName.value = pokemonModel.pokemon.pokemonName.capitalize()
        pokemonHp.value = pokemonModel.pokemon.hp
        pokemonAtk.value = pokemonModel.pokemon.attack
        pokemonDef.value = pokemonModel.pokemon.defense
        pokemonSpeed.value = pokemonModel.pokemon.speed
        pokemonSpAtk.value = pokemonModel.pokemon.specialAttack
        pokemonSpDef.value = pokemonModel.pokemon.specialDefense
        pokemonProfile.value = pokemonModel.pokemon.desc
        pokemonHeight.value = "${(pokemonModel.pokemon.height.toFloat()/10)} m"
        pokemonWeight.value = "${(pokemonModel.pokemon.weight.toFloat()/10)} kg"
//        arrayColorTypes.value = arrayColor
        emit(pokemonModel)
    }
}