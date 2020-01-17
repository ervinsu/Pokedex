package com.ervin.mypokedex.data.local

import androidx.paging.DataSource
import androidx.room.*
import com.ervin.mypokedex.data.local.entity.PokemonEntity
import com.ervin.mypokedex.data.local.entity.PokemonTypeElementEntity
import com.ervin.mypokedex.data.local.entity.type.TypeElementEntity
import com.ervin.mypokedex.data.local.entity.type.effective.TypeElementSuperEffectiveEntityFrom
import com.ervin.mypokedex.data.local.entity.type.effective.TypeElementSuperEffectiveEntityTo
import com.ervin.mypokedex.data.local.entity.type.halfeffective.TypeElementNotEffectiveEntityFrom
import com.ervin.mypokedex.data.local.entity.type.halfeffective.TypeElementNotEffectiveEntityTo
import com.ervin.mypokedex.data.local.entity.type.nodamage.TypeElementNoDamageEntityFrom
import com.ervin.mypokedex.data.local.entity.type.nodamage.TypeElementNoDamageEntityTo
import com.ervin.mypokedex.data.model.PokemonModel
import com.ervin.mypokedex.data.model.SimplePokemonModel
import com.ervin.mypokedex.data.model.SimplePokemonWithTypePojoModel
import com.ervin.mypokedex.data.model.TypeElementModel

@Dao
interface PokemonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPokemon(pokemons: List<PokemonEntity>)

    
    //insert type
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllTypeElement(types: List<TypeElementEntity>)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllTypeElementSuperEffectiveTo(listSuperEffectiveTo: List<TypeElementSuperEffectiveEntityTo>)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllTypeElementNotEffectiveTo(listNotEffectiveTo: List<TypeElementNotEffectiveEntityTo>)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllTypeElementNoDamageTo(listNoDamageTo: List<TypeElementNoDamageEntityTo>)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllTypeElementSuperEffectiveFrom(listSuperEffectiveFrom: List<TypeElementSuperEffectiveEntityFrom>)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllTypeElementNotEffectiveFrom(listNotEffectiveFrom: List<TypeElementNotEffectiveEntityFrom>)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllTypeElementNoDamageFrom(listNoDamageFrom: List<TypeElementNoDamageEntityFrom>)

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

    @Transaction
    @Query("SELECT pokemonId, pokemonName, pokemonSpritesUrl from PokemonTable WHERE pokemonSpritesUrl NOT in ('') ORDER by RANDOM() LIMIT 1")
//    @Query("SELECT pokemonId, pokemonName, pokemonSpritesUrl from PokemonTable WHERE pokemonSpritesUrl NOT in ('') AND pokemonName like '%pikachu%' LIMIT 1")
    suspend fun getRandomSimplePokemon(): SimplePokemonModel

    @Transaction
    @Query("SELECT pokemonName from PokemonTable order by RANDOM() limit 4")
    suspend fun getRandomAnswer(): List<String>

}