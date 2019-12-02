package com.ervin.mypokedex.data.local

import androidx.paging.DataSource
import androidx.room.*
import com.ervin.mypokedex.data.local.entity.PokemonEntity
import com.ervin.mypokedex.data.local.entity.PokemonTypeElementEntity
import com.ervin.mypokedex.data.model.SimplePokemonWithTypePojo
import com.ervin.mypokedex.data.local.entity.TypeElementEntity

@Dao
interface PokemonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPokemon(pokemons: List<PokemonEntity>)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllTypeElement(types: List<TypeElementEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompositePokemonType(pokemonWithTypes: List<PokemonTypeElementEntity>)

    @Transaction
    @Query("SELECT pokemonId, pokemonName, pokemonSpritesUrl from PokemonTable")
    fun getPokemonWithType() : DataSource.Factory<Int, SimplePokemonWithTypePojo>

//    @Transaction
//    @Query("SELECT * from PokemonTable")
//    suspend fun getPokemonWithTypeNonPagedList() : List<PokemonWithTypePojo>

    @Transaction
    @Query("SELECT COUNT(PokemonId) from PokemonTable")
    suspend fun getCountPokemon(): Int

    @Transaction
    @Query("SELECT COUNT(TypeClassId) from TypeTable")
    suspend fun getCountPokemonTypes(): Int

}