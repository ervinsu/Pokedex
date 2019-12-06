package com.ervin.mypokedex.data.local

import androidx.paging.DataSource
import androidx.room.*
import com.ervin.mypokedex.data.local.entity.*
import com.ervin.mypokedex.data.model.PokemonModel
import com.ervin.mypokedex.data.model.SimplePokemonWithTypePojoModel
import com.ervin.mypokedex.data.model.TypeElementModel

@Dao
interface PokemonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPokemon(pokemons: List<PokemonEntity>)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllTypeElement(types: List<TypeElementEntity>)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllTypeElementSuperEffective(types: List<TypeElementSuperEffectiveEntity>)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllTypeElementNotEffective(types: List<TypeElementNotEffectiveEntity>)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllTypeElementNoDamage(types: List<TypeElementNoDamageEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompositePokemonType(pokemonWithTypes: List<PokemonTypeElementEntity>)

    @Transaction
    @Query("SELECT pokemonId, pokemonName, pokemonSpritesUrl from PokemonTable WHERE pokemonName like '%' ||:pokeName || '%'")
    fun getPokemonWithType(pokeName: String): DataSource.Factory<Int, SimplePokemonWithTypePojoModel>

    @Transaction
    @Query("SELECT * from TypeTable")
    suspend fun getTypePokemon(): List<TypeElementModel>

    @Transaction
    @Query("SELECT * from PokemonTable WHERE pokemonId = :pokemonId1")
    suspend fun getSpecificPokemon(pokemonId1: Int): PokemonModel

    @Transaction
    @Query("SELECT COUNT(PokemonId) from PokemonTable")
    suspend fun getCountPokemon(): Int

    @Transaction
    @Query("SELECT COUNT(TypeClassId) from TypeTable")
    suspend fun getCountPokemonTypes(): Int

}