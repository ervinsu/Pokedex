package com.ervin.mypokedex.data

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.Config
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.ervin.mypokedex.data.local.LocalRepository
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
import com.ervin.mypokedex.data.remote.RemoteRepository
import com.ervin.mypokedex.data.remote.entity.ListAPIPokemonResponse
import com.ervin.mypokedex.service.LaunchAppService
import com.ervin.mypokedex.utils.isServiceRunning

class PokemonRepository(
    private val remoteRepository: RemoteRepository,
    private val localRepository: LocalRepository,
    private val context: Context
) {


    companion object {
        @Volatile
        var INSTANCE: PokemonRepository? = null


        fun getInstance(
            remoteRepository: RemoteRepository,
            localRepository: LocalRepository,
            context: Context
        ): PokemonRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = PokemonRepository(remoteRepository, localRepository, context)
                INSTANCE = instance
                instance
            }
        }
    }

    suspend fun saveLocalPokemons(
        listPokemon: List<PokemonEntity>,
        listCompositePokemon: List<PokemonTypeElementEntity>
    ) {
        Log.d("test_service","saved pokemons ${listPokemon.size}")
        try {
            localRepository.insertPokemon(listPokemon)
            localRepository.insertCompositePokemonType(listCompositePokemon)
        }catch (e:Exception){
            Log.d("test",e.message.toString())
        }
    }

    suspend fun saveLocalTypes(
        listType: List<TypeElementEntity>,
        listTypeEffectiveTo: List<TypeElementSuperEffectiveEntityTo>,
        listTypeNotEffectiveTo: List<TypeElementNotEffectiveEntityTo>,
        listTypeNoDamageTo: List<TypeElementNoDamageEntityTo>,
        listTypeEffectiveFrom: List<TypeElementSuperEffectiveEntityFrom>,
        listTypeNotEffectiveFrom: List<TypeElementNotEffectiveEntityFrom>,
        listTypeNoDamageFrom: List<TypeElementNoDamageEntityFrom>
        ) {
        localRepository.insertPokemonType(
            listType,
            listTypeEffectiveTo,
            listTypeNotEffectiveTo,
            listTypeNoDamageTo,
            listTypeEffectiveFrom,
            listTypeNotEffectiveFrom,
            listTypeNoDamageFrom
        )
    }

    suspend fun getRemoteCountPokemonFromRepo(): String {
        return remoteRepository.getCountPokemonRepo()
    }

    suspend fun getRemotePokemonTypes(): ListAPIPokemonResponse {
        return remoteRepository.loadPokemonTypes()
    }

    suspend fun getLocalCountPokemonTypes(): Int {
        return localRepository.loadCountPokemonTypes()
    }


    suspend fun isPokemonAvailable(countRemotePokemon: Int) {
        if (getLocalCountPokemon() != countRemotePokemon && !isServiceRunning(LaunchAppService::class.java)) {
            val intent = Intent(context, LaunchAppService::class.java)
            intent.putExtra("offset", getLocalCountPokemon())
            intent.putExtra("limit", countRemotePokemon)
//            intent.putExtra("limit", 15)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }
//        } 
    }

    suspend fun getRemotePokemonLimit(offset: Int, limit: Int): ListAPIPokemonResponse {
        return remoteRepository.loadPokemons(offset, limit)
    }

    fun getLocalPokemon(pokeName: String): LiveData<PagedList<SimplePokemonWithTypePojoModel>> {
        val myPagingConfig = Config(
            pageSize = 50,
            prefetchDistance = 150,
            enablePlaceholders = true
        )
        return LivePagedListBuilder(localRepository.getPokemonWithType(pokeName), myPagingConfig).build()
    }

    suspend fun getSpecificPokemon(pokemonId: Int): PokemonModel {
        return localRepository.getSpecificPokemon(pokemonId)
    }

    suspend fun getTypePokemon(): List<TypeElementModel> {
        return localRepository.getTypePokemon()
    }

    suspend fun getRandomLocalSimplePokemon(): SimplePokemonModel{
        return localRepository.getRandomSimplePokemon()
    }

    suspend fun getRandomAnswer(): List<String> {
        return localRepository.getRandomAnswer()
    }


    private suspend fun getLocalCountPokemon(): Int {
        return localRepository.getCountPokemon()
    }
}