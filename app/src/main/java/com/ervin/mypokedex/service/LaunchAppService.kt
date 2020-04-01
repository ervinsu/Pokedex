package com.ervin.mypokedex.service

import android.app.Notification
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.ervin.mypokedex.App
import com.ervin.mypokedex.R
import com.ervin.mypokedex.data.local.entity.PokemonEntity
import com.ervin.mypokedex.data.local.entity.PokemonTypeElementEntity
import com.ervin.mypokedex.di.Injection
import com.ervin.mypokedex.utils.ONGOING_CHANNEL_ID
import com.ervin.mypokedex.utils.ON_LOADING_POKEMON
import com.ervin.mypokedex.utils.cancelNotification
import com.ervin.mypokedex.utils.getNotificationBuilder
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import me.sargunvohra.lib.pokekotlin.client.PokeApiClient


class LaunchAppService : Service() {
    private var currentNotify = 0
    private val job = Job()
    private lateinit var manager: NotificationManager
    private var maxLimit: Int = 0
    private val pokeRepo by lazy {
       Injection.provideRepository(App())
    }
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    @ExperimentalCoroutinesApi
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationOnProgress()

        val offset = intent?.getIntExtra("offset", 0)?:0
        val limit = intent?.getIntExtra("limit", 0)?:0 - offset
        Log.d("tag", "$offset $limit")
        var count = offset
        val currLimitPerFetch = 10
        val lastLimitFetch = limit % currLimitPerFetch
        val maxOffset = limit - lastLimitFetch

        CoroutineScope(job + Dispatchers.Main).launch {

             val pokemonListChannel = produce(CoroutineName("db")){
                while (count < maxOffset){
                    send(InnerOffsetPokemon(count,currLimitPerFetch))
                    count += currLimitPerFetch
                }
                //add to channel for last limit
                send(InnerOffsetPokemon(count, lastLimitFetch))
            }

            try {
                withContext(Dispatchers.IO){
                    launch(CoroutineName("launcher-1")) { getQueueRemotePokemon(pokemonListChannel)  }
                    launch(CoroutineName("launcher-2")) { getQueueRemotePokemon(pokemonListChannel)  }
                    launch(CoroutineName("launcher-3")) { getQueueRemotePokemon(pokemonListChannel)  }
                    launch(CoroutineName("launcher-4")) { getQueueRemotePokemon(pokemonListChannel)  }
                    launch(CoroutineName("launcher-5")) { getQueueRemotePokemon(pokemonListChannel)  }
                    launch(CoroutineName("launcher-6")) { getQueueRemotePokemon(pokemonListChannel)  }
                    launch(CoroutineName("launcher-7")) { getQueueRemotePokemon(pokemonListChannel)  }
                    launch(CoroutineName("launcher-8")) { getQueueRemotePokemon(pokemonListChannel)  }
                    launch(CoroutineName("launcher-9")) { getQueueRemotePokemon(pokemonListChannel)  }
                    launch(CoroutineName("launcher-10")) { getQueueRemotePokemon(pokemonListChannel)  }
                }
            } catch (e: Exception) {
                Log.d("exceptionFetchPokemon", e.message.toString())
                sendFeedbackToActivity(false)
            }
            cancelNotification(currentNotify)
            stopSelf()
            stopForeground(true)
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private suspend fun getQueueRemotePokemon(receiveQueuePokemons: ReceiveChannel<InnerOffsetPokemon>){
        for(queuePokemons in receiveQueuePokemons){
            val pokeApi = PokeApiClient()
            val pokemonEntities: MutableList<PokemonEntity> = ArrayList()
            val pokemonCompositeType: MutableList<PokemonTypeElementEntity> = ArrayList()
            val retrievedList = pokeApi.getPokemonList(queuePokemons.offset, queuePokemons.limit)
            retrievedList.results.forEach { simplePokemonResponse ->
                val pokemonID = simplePokemonResponse.id
                val pokemon = pokeApi.getPokemon(pokemonID)
                Log.d(TAG, pokemon.id.toString())

                try {
                    val objectPoke = PokemonEntity(
                        pokemon.id,
                        pokemon.name,
                        pokemon.sprites.frontDefault ?: "",
                        pokemon.stats[0].baseStat,
                        pokemon.stats[1].baseStat,
                        pokemon.stats[2].baseStat,
                        pokemon.stats[3].baseStat,
                        pokemon.stats[4].baseStat,
                        pokemon.stats[5].baseStat,
                        pokemon.weight,
                        pokemon.baseExperience,
                        pokemon.height
                    )
                    Log.d(TAG, pokemon.name)

                    if (pokemon.id < 10000) {
                        try {
                            for (i in pokeApi.getPokemonSpecies(pokemon.id).flavorTextEntries.indices) {
                                if (pokeApi.getPokemonSpecies(pokemon.id).flavorTextEntries[i].language.name == "en") {
                                    val pokeDesc =
                                        pokeApi.getPokemonSpecies(pokemon.id).flavorTextEntries[i].flavorText.replace("\n", " ")
                                    objectPoke.desc = pokeDesc
                                    break
                                }
                            }
                        } catch (e: java.lang.Exception) {
                            Log.d("errordesc", "noData $e")
                        }
                    }
                    //add to table pokemon
                    pokemonEntities.add(
                        objectPoke
                    )

                    //add to composite table types pokemon
                    pokemon.types.forEach { pokemonType ->
                        pokemonCompositeType.add(
                            PokemonTypeElementEntity(
                                pokemon.id,
                                pokemonType.type.id
                            )
                        )
                    }
                }catch (e:java.lang.Exception){
                    Log.d(TAG, e.message.toString())
                }
            }
            pokeRepo.saveLocalPokemons(pokemonEntities, pokemonCompositeType)
//            manager.notify(ON_LOADING_POKEMON,updateNotificationForRemoteOnProgress(maxOffset/currLimitPerFetch, i))
        }
    }

    private suspend fun getRemotePokemon(offset: Int, limit: Int) {
//        val retrievedList = pokeRepo.getRemotePokemonLimit(offset, limit)
        val pokeApi = PokeApiClient()
        val pokemonEntities: MutableList<PokemonEntity> = ArrayList()
        val pokemonCompositeType: MutableList<PokemonTypeElementEntity> = ArrayList()

        val retrievedList = pokeApi.getPokemonList(offset, limit)
        retrievedList.results.forEach { simplePokemonResponse ->
            val pokemonID = simplePokemonResponse.id
            val pokemon = pokeApi.getPokemon(pokemonID)
            Log.d(TAG, pokemon.id.toString())

            try {
                val objectPoke = PokemonEntity(
                    pokemon.id,
                    pokemon.name,
                    pokemon.sprites.frontDefault ?: "",
                    pokemon.stats[0].baseStat,
                    pokemon.stats[1].baseStat,
                    pokemon.stats[2].baseStat,
                    pokemon.stats[3].baseStat,
                    pokemon.stats[4].baseStat,
                    pokemon.stats[5].baseStat,
                    pokemon.weight,
                    pokemon.baseExperience,
                    pokemon.height
                )
                Log.d(TAG, pokemon.name)

                if (pokemon.id < 10000) {
                    try {
                        for (i in pokeApi.getPokemonSpecies(pokemon.id).flavorTextEntries.indices) {
                            if (pokeApi.getPokemonSpecies(pokemon.id).flavorTextEntries[i].language.name == "en") {
                                val pokeDesc =
                                    pokeApi.getPokemonSpecies(pokemon.id).flavorTextEntries[i].flavorText.replace("\n", " ")
                                objectPoke.desc = pokeDesc
                                break
                            }
                        }
                    } catch (e: java.lang.Exception) {
                        Log.d("errordesc", "noData $e")
                    }
                }
                //add to table pokemon
                pokemonEntities.add(
                    objectPoke
                )

                //add to composite table types pokemon
                pokemon.types.forEach { pokemonType ->
                    pokemonCompositeType.add(
                        PokemonTypeElementEntity(
                            pokemon.id,
                            pokemonType.type.id
                        )
                    )
                }
            }catch (e:java.lang.Exception){
                Log.d(TAG, e.message.toString())
            }
        }

//        manager.notify(
//            ON_LOADING_POKEMON,
//            updateNotificationForRemoteOnProgress(maxLimit / (1+(offset/100)), maxLimit)
//        )

//        Log.d(TAG,"notification limit ${maxLimit / (1+(offset/100))}, $maxLimit")

        pokeRepo.saveLocalPokemons(pokemonEntities, pokemonCompositeType)
    }

    private fun createNotificationOnProgress() {
        currentNotify = ON_LOADING_POKEMON
        // create notification and register the channel with the app
        val builder = getNotificationBuilder(
            ONGOING_CHANNEL_ID,
            NotificationManagerCompat.IMPORTANCE_HIGH
        ).apply {
            setOngoing(true)
            setSmallIcon(R.drawable.ic_launcher_foreground)
            setContentTitle("Data Loading")
            setProgress(0, 0, true)
        }
        val notification = builder.build()
        startForeground(ON_LOADING_POKEMON, notification)
    }

    private fun updateNotificationForRemoteOnProgress(max:Int, progress:Int):Notification{
        val builder = getNotificationBuilder(
            ONGOING_CHANNEL_ID,
            NotificationManagerCompat.IMPORTANCE_LOW
        ).apply {
            setOngoing(true)
            setSmallIcon(R.drawable.ic_launcher_foreground)
            setContentTitle("Data Loading")
            setProgress(max, progress, false)
        }
       return builder.build()
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }

    private fun sendFeedbackToActivity(boolean: Boolean) {
        val intent = Intent(INTENT_FILTER_SERVICE_GET_POKEMON)
        intent.putExtra(RESULT_FETCHING_POKEMON, boolean)
        LocalBroadcastManager.getInstance(App().getContext()).sendBroadcast(intent)
    }

    companion object {
        private const val TAG = "TEST_SERVICE"
        const val RESULT_FETCHING_POKEMON = "RESULT_FETCH"
        const val INTENT_FILTER_SERVICE_GET_POKEMON = "INTENT_FILTER_GET_POKEMON"
    }

    data class InnerOffsetPokemon(
        val offset: Int,
        val limit: Int
    )


}