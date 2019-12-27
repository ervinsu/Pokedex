package com.ervin.mypokedex.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import com.ervin.mypokedex.utils.TablePokemonEvolveTo

@Entity(
    tableName = TablePokemonEvolveTo.PokemonEvolveTable,
    primaryKeys = [TablePokemonEvolveTo.PokemonId,
        TablePokemonEvolveTo.PokemonIdEvolveTo],
    indices = [Index(value = [TablePokemonEvolveTo.PokemonId,
        TablePokemonEvolveTo.PokemonIdEvolveTo])]
)
data class PokemonEvolveEntity (
    @ColumnInfo(name = TablePokemonEvolveTo.PokemonId)
    val ck_pokemonId: Int,

    @ColumnInfo(name = TablePokemonEvolveTo.PokemonIdEvolveTo)
    val ck_pokemonIdEvolveTo: Int,

    @ColumnInfo(name = TablePokemonEvolveTo.PokemonEvolveRequire)
    val ck_pokemonEvolveRequire: String
)