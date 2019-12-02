package com.ervin.mypokedex.data.model

import androidx.room.ColumnInfo
import com.ervin.mypokedex.utils.TablePokemon

data class SimplePokemon (
    @ColumnInfo(name = TablePokemon.PokemonID)
    val pokemonId: Int,

    @ColumnInfo(name = TablePokemon.PokemonName)
    val pokemonName: String,

    @ColumnInfo(name = TablePokemon.PokemonSprites)
    val pokemonSpritesUrl: String)