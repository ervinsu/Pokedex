package com.ervin.mypokedex.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import com.ervin.mypokedex.utils.TablePokemonTypeElement

@Entity(
    tableName = TablePokemonTypeElement.PokemonTypeElementTable,
    primaryKeys = [TablePokemonTypeElement.PokemonTypeElementPokemonID,
        TablePokemonTypeElement.PokemonTypeElementID],
    indices = [Index(value = [TablePokemonTypeElement.PokemonTypeElementPokemonID,
        TablePokemonTypeElement.PokemonTypeElementID])]
    )
data class PokemonTypeElementEntity(
    @ColumnInfo(name = TablePokemonTypeElement.PokemonTypeElementPokemonID, index = true)
    val ck_pokemonId: Int,
    @ColumnInfo(name = TablePokemonTypeElement.PokemonTypeElementID, index = true)
    val ck_typeId: Int
)