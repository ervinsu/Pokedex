package com.ervin.mypokedex.data.local

import androidx.paging.DataSource
import com.ervin.mypokedex.data.local.entity.PokemonEntity
import com.ervin.mypokedex.data.local.entity.PokemonTypeElementEntity
import com.ervin.mypokedex.data.model.SimplePokemonWithTypePojo
import com.ervin.mypokedex.data.local.entity.TypeElementEntity

class LocalRepository(private val dao: PokemonDao) {

    companion object {
        private var INSTACE: LocalRepository? = null
        fun getInstance(pokemonDao: PokemonDao): LocalRepository{
            return INSTACE?: LocalRepository(pokemonDao)
        }
    }

    suspend fun insertPokemonType(data: List<TypeElementEntity>) {
        dao.insertAllTypeElement(data)
    }

    suspend fun insertPokemon(data: List<PokemonEntity>) {
        dao.insertAllPokemon(data)
    }

    suspend fun insertCompositePokemonType(data: List<PokemonTypeElementEntity>){
        dao.insertCompositePokemonType(data)
    }

    fun getPokemonWithType(): DataSource.Factory<Int,SimplePokemonWithTypePojo> {
        return dao.getPokemonWithType()
    }

//    suspend fun getPokemonWithTypeNonPagedList(): List<PokemonWithTypePojo>{
//        return dao.getPokemonWithTypeNonPagedList()
//    }

    suspend fun getCountPokemon(): Int{
        return dao.getCountPokemon()
    }

    suspend fun loadCountPokemonTypes(): Int {
        return dao.getCountPokemonTypes()
    }


}