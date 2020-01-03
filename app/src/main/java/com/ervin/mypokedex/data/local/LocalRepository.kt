package com.ervin.mypokedex.data.local

import androidx.paging.DataSource
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
import com.ervin.mypokedex.data.model.SimplePokemonWithTypePojoModel
import com.ervin.mypokedex.data.model.TypeElementModel

class LocalRepository(private val dao: PokemonDao) {

    companion object {
        private var INSTACE: LocalRepository? = null
        fun getInstance(pokemonDao: PokemonDao): LocalRepository {
            return INSTACE ?: LocalRepository(pokemonDao)
        }
    }

    suspend fun insertPokemonType(
        data: List<TypeElementEntity>,
        listTypeEffectiveTo: List<TypeElementSuperEffectiveEntityTo>,
        listTypeNotEffectiveTo: List<TypeElementNotEffectiveEntityTo>,
        listTypeNoDamageTo: List<TypeElementNoDamageEntityTo>,
        listTypeEffectiveFrom: List<TypeElementSuperEffectiveEntityFrom>,
        listTypeNotEffectiveFrom: List<TypeElementNotEffectiveEntityFrom>,
        listTypeNoDamageFrom: List<TypeElementNoDamageEntityFrom>
    ) {
        dao.insertAllTypeElement(data)
        dao.insertAllTypeElementSuperEffectiveTo(listTypeEffectiveTo)
        dao.insertAllTypeElementNotEffectiveTo(listTypeNotEffectiveTo)
        dao.insertAllTypeElementNoDamageTo(listTypeNoDamageTo)
        dao.insertAllTypeElementSuperEffectiveFrom(listTypeEffectiveFrom)
        dao.insertAllTypeElementNotEffectiveFrom(listTypeNotEffectiveFrom)
        dao.insertAllTypeElementNoDamageFrom(listTypeNoDamageFrom)
    }

    suspend fun insertPokemon(data: List<PokemonEntity>) {
        dao.insertAllPokemon(data)
    }

    suspend fun insertCompositePokemonType(data: List<PokemonTypeElementEntity>) {
        dao.insertCompositePokemonType(data)
    }

    fun getPokemonWithType(pokeName: String): DataSource.Factory<Int, SimplePokemonWithTypePojoModel> {
        return dao.getPokemonWithType(pokeName)
    }

    suspend fun getSpecificPokemon(pokemonId: Int): PokemonModel {
        return dao.getSpecificPokemon(pokemonId)
    }

    suspend fun getTypePokemon(): List<TypeElementModel> {
        return dao.getTypePokemon()
    }

    suspend fun getCountPokemon(): Int {
        return dao.getCountPokemon()
    }

    suspend fun loadCountPokemonTypes(): Int {
        return dao.getCountPokemonTypes()
    }


}