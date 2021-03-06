package com.ervin.mypokedex

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.lifecycle.*
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.paging.PagedList
import com.ervin.mypokedex.data.PokemonRepository
import com.ervin.mypokedex.data.local.entity.TypeElementEntity
import com.ervin.mypokedex.data.model.SimplePokemonWithTypePojo
import com.ervin.mypokedex.service.LaunchAppService.Companion.INTENT_FILTER_SERVICE_GET_POKEMON
import com.ervin.mypokedex.service.LaunchAppService.Companion.RESULT_FETCHING_POKEMON
import com.ervin.mypokedex.utils.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.sargunvohra.lib.pokekotlin.client.PokeApiClient
import org.json.JSONObject

class MainViewModel (private val pokemonRepository: PokemonRepository): ViewModel() {
    private var countPokemon = MutableLiveData<Int>()
    private val booleanFetch = MutableLiveData<Boolean>()
    private val isDataLoaded = MutableLiveData<Boolean>(false)
    private var currOffset = 0

    private val mMessageReceiver = object: BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            booleanFetch.value = intent?.getBooleanExtra(RESULT_FETCHING_POKEMON,false)
        }
    }

    init {
        LocalBroadcastManager.getInstance(App().getContext()).registerReceiver(
            mMessageReceiver, IntentFilter(INTENT_FILTER_SERVICE_GET_POKEMON)
        )
    }

    fun setIsDataLoaded(boolean: Boolean){
        isDataLoaded.value = boolean
    }

    fun getIsDataLoaded():LiveData<Boolean>{
        return isDataLoaded
    }

//    private val listPokemonPojo: LiveData<List<PokemonWithTypePojo>> = Transformations.switchMap(countPokemon){ countPokemon->
//
//        liveData(Dispatchers.IO) {
//            val processedList:MutableList<Pokemon> = ArrayList()
//            try {
//                val retrievedList = pokemonRepository.loadListPokemon()
//                val pokeApi = PokeApiClient()
//                retrievedList.listResponseAPI.forEach { simplePokemonResponse->
//                    val pokemonID = simplePokemonResponse.urlResponse.split("/")[6].toInt()
//                    Log.d("test",pokemonID.toString())
//                    processedList.add(pokeApi.getPokemon(pokemonID))
//                }
//                val pokemonEntities: MutableList<PokemonEntity> = ArrayList()
//                val pokemonCompositeType: MutableList<PokemonTypeElementEntity> = ArrayList()
//                processedList.forEach{pokemon->
////                    pokemonEntities.add(
////                        PokemonEntity(
////                            pokemon.id,
////                            pokemon.name,
////                            pokemon.sprites.frontDefault ?: ""
////                        )
////                    )
//                    pokemon.types.forEach{pokemonType->
//                        pokemonCompositeType.add(
//                            PokemonTypeElementEntity(
//                                pokemon.id,
//                                pokemonType.type.id
//                            )
//                        )
//                    }
//                }
//                pokemonRepository.saveLocalPokemons(pokemonEntities,pokemonCompositeType)
////                emit(pokemonRepository.getLocalPokemonWithType())
//            }catch (e:Exception){
//                Log.d("errorListPokemon",e.message.toString())
//            }
//        }
//    }

    fun getFetchBoolean():LiveData<Boolean>{
        return booleanFetch
    }


    suspend fun getCountRemotePokemon():LiveData<Int>{
        viewModelScope.launch (Dispatchers.IO){
            val pokeSize: Int
            try {
                val jsonConvert = JSONObject(pokemonRepository.getRemoteCountPokemonFromRepo())
                try {
                    pokeSize = jsonConvert.getInt("count")
                    withContext(Dispatchers.Main) {
                        countPokemon.value = pokeSize
                    }
                }catch (e:java.lang.Exception){
                    Log.d("getCountException", e.message.toString())
                }
            }catch (e:java.lang.Exception){
                Log.d("getCountRetroException", e.message.toString())
            }
        }
        return countPokemon
    }

    suspend fun getCountLocalPokemon():Int{
        return pokemonRepository.getLocalCountPokemon()
    }

    fun loadRemotePokemons1():LiveData<Response<PagedList<SimplePokemonWithTypePojo>>>{
//        pokemonRepository.isPokemonAvailable(55)
        val data = MutableLiveData<Response<PagedList<SimplePokemonWithTypePojo>>>()

        return Transformations.switchMap(refreshLocalPokemon()) { pagedList ->
            data.value = Response.success(pagedList)
            data
        }
    }

    fun loadRemotePokemons(): Flow<Boolean> = flow{
        val returned: Boolean = try {
            pokemonRepository.isPokemonAvailable(52)
            true
        }catch (e:java.lang.Exception){
            false
        }
        emit(returned)
    }

    fun updateRemotePokemons(): Flow<Boolean> = flow{
        val returned: Boolean = try {
            pokemonRepository.isPokemonAvailable(252)
            true
        }catch (e:java.lang.Exception){
            false
        }
        emit(returned)
    }

    private fun refreshLocalPokemon():LiveData<PagedList<SimplePokemonWithTypePojo>>{
        return pokemonRepository.getLocalPokemon()
    }

    fun getLocalPokemon():LiveData<Response<PagedList<SimplePokemonWithTypePojo>>>{
        val data = MutableLiveData<Response<PagedList<SimplePokemonWithTypePojo>>>()

        return Transformations.switchMap(refreshLocalPokemon()) { pagedList ->
            data.value = Response.success(pagedList)
            data
        }
    }

    fun loadRemoteTypesPokemon(): Flow<Boolean> = flow{
        var returned = false
        if(pokemonRepository.getLocalCountPokemonTypes() == 0) {
            try {
                val elementEntities: MutableList<TypeElementEntity> = ArrayList()
                val pokeTypes = pokemonRepository.getRemotePokemonTypes().listResponseAPI
                pokeTypes.forEach { type ->
                    val color = when (type.nameResponse) {
                        "psychic" -> "#F95587"
                        "normal" -> "#A8A77A"
                        "fighting" -> "#C22E28"
                        "flying" -> "#A98FF3"
                        "poison" -> "#A33EA1"
                        "ground" -> "#E2BF65"
                        "rock" -> "#B6A136"
                        "ghost" -> "#735797"
                        "steel" -> "#B7B7CE"
                        "bug" -> "#A6B91A"
                        "fire" -> "#EE8130"
                        "water" -> "#6390F0"
                        "grass" -> "#7AC74C"
                        "electric" -> "#F7D02C"
                        "ice" -> "#96D9D6"
                        "dragon" -> "#6F35FC"
                        "dark" -> "#705746"
                        "fairy" -> "#D685AD"
                        else -> "#"
                    }
                    val typeElementId = type.urlResponse.split("/")[6].toInt()
                    elementEntities.add(
                        TypeElementEntity(
                            typeElementId,
                            type.nameResponse,
                            color,
                            type.urlResponse
                        )
                    )
                }
                pokemonRepository.saveLocalTypes(elementEntities)
                returned = true
            } catch (e: java.lang.Exception) {
                returned = false
            }
        }
        emit(returned)
    }

    override fun onCleared() {
        LocalBroadcastManager.getInstance(App().getContext()).unregisterReceiver(mMessageReceiver)
        super.onCleared()
    }

    init {
        viewModelScope.launch {
            simpleFetch()
        }


    }
    private suspend fun simpleFetch(){
        withContext(Dispatchers.IO){
            try {
                val pokeApi = PokeApiClient()
                val b = pokeApi.getPokemonList(5,6)
                b.results[0].id
            }catch (e:java.lang.Exception){
                Log.d("tess",e.message.toString())
            }
           //            println(bulbasaur)
        }
    }
}