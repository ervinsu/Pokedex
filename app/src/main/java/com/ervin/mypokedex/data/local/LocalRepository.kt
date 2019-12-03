package com.ervin.mypokedex.data.local

import androidx.paging.DataSource
import com.ervin.mypokedex.data.local.entity.*
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
        listTypeEffective: List<TypeElementSuperEffectiveEntity>,
        listTypeNotEffective: List<TypeElementNotEffectiveEntity>,
        listTypeNoDamage: List<TypeElementNoDamageEntity>
    ) {
        dao.insertAllTypeElement(data)
        dao.insertAllTypeElementSuperEffective(listTypeEffective)
        dao.insertAllTypeElementNotEffective(listTypeNotEffective)
        dao.insertAllTypeElementNoDamage(listTypeNoDamage)
    }

    suspend fun insertPokemon(data: List<PokemonEntity>) {
        dao.insertAllPokemon(data)
    }

    suspend fun insertCompositePokemonType(data: List<PokemonTypeElementEntity>) {
        dao.insertCompositePokemonType(data)
    }

    fun getPokemonWithType(): DataSource.Factory<Int, SimplePokemonWithTypePojoModel> {
        return dao.getPokemonWithType()
    }

    suspend fun getTypePokemon(): List<TypeElementModel>{
        return dao.getTypePokemon()
    }

    suspend fun getCountPokemon(): Int {
        return dao.getCountPokemon()
    }

    suspend fun loadCountPokemonTypes(): Int {
        return dao.getCountPokemonTypes()
    }


}