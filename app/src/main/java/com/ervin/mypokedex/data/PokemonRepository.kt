package com.ervin.mypokedex.data

import android.content.Intent
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.paging.Config
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.ervin.mypokedex.App
import com.ervin.mypokedex.data.local.LocalRepository
import com.ervin.mypokedex.data.local.entity.*
import com.ervin.mypokedex.data.model.SimplePokemonWithTypePojoModel
import com.ervin.mypokedex.data.model.TypeElementModel
import com.ervin.mypokedex.data.remote.RemoteRepository
import com.ervin.mypokedex.data.remote.entity.ListAPIPokemonResponse
import com.ervin.mypokedex.service.LaunchAppService

class PokemonRepository(
    private val remoteRepository: RemoteRepository,
    private val localRepository: LocalRepository
) {


    companion object {
        @Volatile
        var INSTANCE: PokemonRepository? = null


        fun getInstance(
            remoteRepository: RemoteRepository,
            localRepository: LocalRepository
        ): PokemonRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = PokemonRepository(remoteRepository, localRepository)
                INSTANCE = instance
                instance
            }
        }
    }

    suspend fun saveLocalPokemons(
        listPokemon: List<PokemonEntity>,
        listCompositePokemon: List<PokemonTypeElementEntity>
    ) {
        localRepository.insertPokemon(listPokemon)
        localRepository.insertCompositePokemonType(listCompositePokemon)
    }

    suspend fun saveLocalTypes(
        listType: List<TypeElementEntity>,
        listTypeEffective: List<TypeElementSuperEffectiveEntity>,
        listTypeNotEffective: List<TypeElementNotEffectiveEntity>,
        listTypeNoDamage: List<TypeElementNoDamageEntity>
    ) {
        localRepository.insertPokemonType(
            listType,
            listTypeEffective,
            listTypeNotEffective,
            listTypeNoDamage
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
//        CoroutineScope(Dispatchers.IO).launch {
        val countLocalPokemon = localRepository.getCountPokemon()
        if (countLocalPokemon != countRemotePokemon) {
            val intent = Intent(App().getContext(), LaunchAppService::class.java)
            intent.putExtra("offset", countLocalPokemon)
            intent.putExtra("limit", countRemotePokemon)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                App().getContext().startForegroundService(intent)
            } else {
                App().getContext().startService(intent)
            }
        }
//        } 
    }

    suspend fun getRemotePokemonLimit(offset: Int, limit: Int): ListAPIPokemonResponse {
        return remoteRepository.loadPokemons(offset, limit)
    }

    fun getLocalPokemon(): LiveData<PagedList<SimplePokemonWithTypePojoModel>> {
        val myPagingConfig = Config(
            pageSize = 50,
            prefetchDistance = 150,
            enablePlaceholders = true
        )
        return LivePagedListBuilder(localRepository.getPokemonWithType(), myPagingConfig).build()
    }

    suspend fun getTypePokemon():List<TypeElementModel>{
        return localRepository.getTypePokemon()
    }


    suspend fun getLocalCountPokemon(): Int {
        return localRepository.getCountPokemon()
    }
}