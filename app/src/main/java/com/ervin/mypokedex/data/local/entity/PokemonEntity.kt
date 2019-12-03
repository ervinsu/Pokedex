package com.ervin.mypokedex.data.local.entity


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.ervin.mypokedex.utils.TablePokemon

@Entity
    (tableName = TablePokemon.PokemonTable,
    indices = [Index(TablePokemon.PokemonID)])
data class PokemonEntity(
    @PrimaryKey
    @ColumnInfo(name = TablePokemon.PokemonID)
    val pokemonId: Int,

    @ColumnInfo(name = TablePokemon.PokemonName)
    val pokemonName: String,

    @ColumnInfo(name = TablePokemon.PokemonSprites)
    val pokemonSpritesUrl: String,

    @ColumnInfo(name = TablePokemon.PokemonSpeed)
    val speed: Int,

    @ColumnInfo(name = TablePokemon.PokemonDefense)
    val specialDefense: Int,

    @ColumnInfo(name = TablePokemon.PokemonSpAttack)
    val specialAttack: Int,

    @ColumnInfo(name = TablePokemon.PokemonSpDefense)
    val defense: Int,

    @ColumnInfo(name = TablePokemon.PokemonAttack)
    val attack: Int,

    @ColumnInfo(name = TablePokemon.PokemonHp)
    val hp: Int,

    @ColumnInfo(name = TablePokemon.PokemonWeight)
    val weight: Int,

    @ColumnInfo(name = TablePokemon.PokemonBaseExp)
    val baseExperience: Int,

    @ColumnInfo(name = TablePokemon.PokemonHeight)
    val height: Int

)
