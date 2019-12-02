package com.ervin.mypokedex.data

import android.content.Intent
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.paging.Config
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.ervin.mypokedex.App
import com.ervin.mypokedex.data.local.LocalRepository
import com.ervin.mypokedex.data.local.entity.PokemonEntity
import com.ervin.mypokedex.data.local.entity.PokemonTypeElementEntity
import com.ervin.mypokedex.data.local.entity.TypeElementEntity
import com.ervin.mypokedex.data.model.SimplePokemonWithTypePojo
import com.ervin.mypokedex.data.remote.RemoteRepository
import com.ervin.mypokedex.data.remote.entity.ListAPIPokemonResponse
import com.ervin.mypokedex.service.LaunchAppService
import com.ervin.mypokedex.utils.AppExecutors

class PokemonRepository(private val remoteRepository: RemoteRepository, private val localRepository: LocalRepository, private val app: AppExecutors) {


    companion object{
        @Volatile
        var INSTANCE: PokemonRepository? = null


        fun getInstance(remoteRepository: RemoteRepository, localRepository: LocalRepository, app: AppExecutors):PokemonRepository{
            return INSTANCE?: synchronized(this){
                val instance = PokemonRepository(remoteRepository, localRepository, app)
                INSTANCE = instance
                instance
            }
        }
    }

    suspend fun saveLocalPokemons(listPokemon: List<PokemonEntity>, listCompositePokemon: List<PokemonTypeElementEntity> ){
        localRepository.insertPokemon(listPokemon)
        localRepository.insertCompositePokemonType(listCompositePokemon)
    }

    suspend fun saveLocalTypes(listType: List<TypeElementEntity>){
        localRepository.insertPokemonType(listType)

    }

    suspend fun getRemoteCountPokemonFromRepo():String{
        return remoteRepository.getCountPokemonRepo()
    }

    suspend fun loadListPokemon(): ListAPIPokemonResponse {
        return remoteRepository.loadPokemons(0,50)
    }

    suspend fun getRemotePokemonTypes(): ListAPIPokemonResponse{
        return remoteRepository.loadPokemonTypes()
    }

    suspend fun getLocalCountPokemonTypes(): Int{
        return localRepository.loadCountPokemonTypes()
    }


    suspend fun isPokemonAvailable(countRemotePokemon:Int){
//        CoroutineScope(Dispatchers.IO).launch {
            val countLocalPokemon = localRepository.getCountPokemon()
            if (countLocalPokemon != countRemotePokemon) {
                val intent = Intent(App().getContext(), LaunchAppService::class.java)
                intent.putExtra("offset",countLocalPokemon)
                intent.putExtra("limit",countRemotePokemon)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    App().getContext().startForegroundService(intent)
                }else{
                    App().getContext().startService(intent)
                }
            }
//        } 
    }

    suspend fun getRemotePokemonLimit(offset:Int, limit:Int): ListAPIPokemonResponse{
        return remoteRepository.loadPokemons(offset,limit)
    }

    fun getLocalPokemon(): LiveData<PagedList<SimplePokemonWithTypePojo>> {
        val myPagingConfig = Config(
            pageSize = 50,
            prefetchDistance = 150,
            enablePlaceholders = true
        )
        return LivePagedListBuilder(localRepository.getPokemonWithType(), myPagingConfig).build()
    }

    suspend fun getLocalCountPokemon(): Int {
        return localRepository.getCountPokemon()
    }
}