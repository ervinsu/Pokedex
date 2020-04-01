package com.ervin.mypokedex.ui.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import android.view.View
import androidx.lifecycle.*
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.paging.PagedList
import com.ervin.mypokedex.App
import com.ervin.mypokedex.data.PokemonRepository
import com.ervin.mypokedex.data.local.entity.type.TypeElementEntity
import com.ervin.mypokedex.data.local.entity.type.effective.TypeElementSuperEffectiveEntityFrom
import com.ervin.mypokedex.data.local.entity.type.effective.TypeElementSuperEffectiveEntityTo
import com.ervin.mypokedex.data.local.entity.type.halfeffective.TypeElementNotEffectiveEntityFrom
import com.ervin.mypokedex.data.local.entity.type.halfeffective.TypeElementNotEffectiveEntityTo
import com.ervin.mypokedex.data.local.entity.type.nodamage.TypeElementNoDamageEntityFrom
import com.ervin.mypokedex.data.local.entity.type.nodamage.TypeElementNoDamageEntityTo
import com.ervin.mypokedex.data.model.SimplePokemonWithTypePojoModel
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

class MainViewModel(private val pokemonRepository: PokemonRepository) : ViewModel() {
    private var countPokemon = MutableLiveData<Int>()
    private val booleanFetch = MutableLiveData<Boolean>()
    private val isDataLoaded = MutableLiveData<Boolean>(false)
    var rootView = MutableLiveData<View>()

    private val mMessageReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            booleanFetch.value = intent?.getBooleanExtra(RESULT_FETCHING_POKEMON, false)
        }
    }

    init {
        LocalBroadcastManager.getInstance(App().getContext()).registerReceiver(
            mMessageReceiver, IntentFilter(INTENT_FILTER_SERVICE_GET_POKEMON)
        )
    }

    fun setIsDataLoaded(boolean: Boolean) {
        isDataLoaded.value = boolean
    }

    fun getIsDataLoaded(): LiveData<Boolean> {
        return isDataLoaded
    }

    fun getFetchBoolean(): LiveData<Boolean> {
        return booleanFetch
    }


    suspend fun getCountRemotePokemon(): LiveData<Int> {
        viewModelScope.launch(Dispatchers.IO) {
            val pokeSize: Int
            try {
                val jsonConvert = JSONObject(pokemonRepository.getRemoteCountPokemonFromRepo())
                try {
                    pokeSize = jsonConvert.getInt("count")
                    withContext(Dispatchers.Main) {
                        countPokemon.value = pokeSize
                    }
                } catch (e: java.lang.Exception) {
                    Log.d("getCountException", e.message.toString())
                }
            } catch (e: java.lang.Exception) {
                Log.d("getCountRetroException", e.message.toString())
            }
        }
        return countPokemon
    }

//    fun loadRemotePokemons(): Flow<Boolean> = flow {
//        val returned: Boolean = try {
//            pokemonRepository.isPokemonAvailable(52)
//            true
//        } catch (e: java.lang.Exception) {
//            false
//        }
//        emit(returned)
//    }


    fun loadRemotePokemons2(): LiveData<Boolean> = Transformations.switchMap(countPokemon){count->
        liveData(Dispatchers.IO) {
            Log.d("countPokemon",count.toString())
            val returned: Boolean = try {
                pokemonRepository.isPokemonAvailable(count)
                true
            } catch (e: java.lang.Exception) {
                false
            }
            emit(returned)
        }
    }

//    fun updateRemotePokemons(): Flow<Boolean> = flow {
//        val returned: Boolean = try {
//            pokemonRepository.isPokemonAvailable(252)
//            true
//        } catch (e: java.lang.Exception) {
//            false
//        }
//        emit(returned)
//    }

    private fun refreshLocalPokemon(pokeName: String): LiveData<PagedList<SimplePokemonWithTypePojoModel>> {
        return pokemonRepository.getLocalPokemon(pokeName)
    }

    fun getLocalPokemon(pokeName: String): LiveData<Response<PagedList<SimplePokemonWithTypePojoModel>>> {
        val data = MutableLiveData<Response<PagedList<SimplePokemonWithTypePojoModel>>>()
        return Transformations.switchMap(refreshLocalPokemon(pokeName)) { pagedList ->
            data.value = Response.success(pagedList)
            data
        }
    }

    fun getPokemonType() = liveData {
        emit(pokemonRepository.getTypePokemon())
    }

    fun getSpecificPokemon() = liveData {
        emit(pokemonRepository.getSpecificPokemon(1))
    }

    fun loadRemoteTypesPokemon(): Flow<Boolean> = flow {
        var returned = false
        if (pokemonRepository.getLocalCountPokemonTypes() == 0) {
            try {
                val elementEntities: MutableList<TypeElementEntity> = ArrayList()
                val pokeTypes = pokemonRepository.getRemotePokemonTypes().listResponseAPI
                val pokeApi = PokeApiClient()
                val superEffectiveListTo: MutableList<TypeElementSuperEffectiveEntityTo> = ArrayList()
                val notEffectiveListTo: MutableList<TypeElementNotEffectiveEntityTo> = ArrayList()
                val noDamageListTo: MutableList<TypeElementNoDamageEntityTo> = ArrayList()
                val superEffectiveListFrom: MutableList<TypeElementSuperEffectiveEntityFrom> = ArrayList()
                val notEffectiveListFrom: MutableList<TypeElementNotEffectiveEntityFrom> = ArrayList()
                val noDamageListFrom: MutableList<TypeElementNoDamageEntityFrom> = ArrayList()

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
                    withContext(Dispatchers.IO) {
                        val typePokemonRemote = pokeApi.getType(typeElementId)

                        typePokemonRemote.damageRelations.doubleDamageTo.forEach { doubleDamageTo ->
                            superEffectiveListTo.add(
                                TypeElementSuperEffectiveEntityTo(
                                    typeElementId,
                                    doubleDamageTo.id
                                )
                            )
                        }

                        typePokemonRemote.damageRelations.halfDamageTo.forEach { halfDamageTo ->
                            notEffectiveListTo.add(
                                TypeElementNotEffectiveEntityTo(
                                    typeElementId,
                                    halfDamageTo.id
                                )
                            )
                        }

                        typePokemonRemote.damageRelations.noDamageTo.forEach { noDamageTo ->
                            noDamageListTo.add(
                                TypeElementNoDamageEntityTo(
                                    typeElementId,
                                    noDamageTo.id
                                )
                            )
                        }

                        typePokemonRemote.damageRelations.doubleDamageFrom.forEach { doubleDamageFrom->
                            superEffectiveListFrom.add(
                                TypeElementSuperEffectiveEntityFrom(
                                    typeElementId,
                                    doubleDamageFrom.id
                                )
                            )
                        }

                        typePokemonRemote.damageRelations.halfDamageFrom.forEach { halfDamageFrom->
                            notEffectiveListFrom.add(
                                TypeElementNotEffectiveEntityFrom(
                                    typeElementId,
                                    halfDamageFrom.id
                                )
                            )
                        }

                        typePokemonRemote.damageRelations.noDamageFrom.forEach { noDamageFrom->
                            noDamageListFrom.add(
                                TypeElementNoDamageEntityFrom(
                                    typeElementId,
                                    noDamageFrom.id
                                )
                            )
                        }
                    }
                }
                Log.d(
                    "getpoke",
                    "${elementEntities.size} ${superEffectiveListTo.size} ${notEffectiveListTo.size} ${noDamageListTo.size}"
                )
                pokemonRepository.saveLocalTypes(
                    elementEntities,
                    superEffectiveListTo,
                    notEffectiveListTo,
                    noDamageListTo,
                    superEffectiveListFrom,
                    notEffectiveListFrom,
                    noDamageListFrom
                )
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

    private suspend fun simpleFetch() {
        withContext(Dispatchers.IO) {
            try {
                val pokeApi = PokeApiClient()
                val b = pokeApi.getPokemonList(0, 6)
                val species = pokeApi.getPokemonSpecies(2)
                Log.d("species", "${species.evolvesFromSpecies?.id.toString()} ${species.evolutionChain.category} ${b.results[0].category}")
//                val  chain = species.evolutionChain
//                val evolvechain = pokeApi.getEvolutionChain(1)
//                val chain = evolvechain.chain.evolvesTo.
            } catch (e: java.lang.Exception) {
                Log.d("tess", e.message.toString())
            }
        }
    }
}