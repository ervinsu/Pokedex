package com.ervin.mypokedex.service

import android.app.Notification
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.ervin.mypokedex.App
import com.ervin.mypokedex.R
import com.ervin.mypokedex.data.PokemonRepository
import com.ervin.mypokedex.data.local.entity.PokemonEntity
import com.ervin.mypokedex.data.local.entity.PokemonTypeElementEntity
import com.ervin.mypokedex.di.Injection
import com.ervin.mypokedex.utils.ONGOING_CHANNEL_ID
import com.ervin.mypokedex.utils.ON_LOADING_POKEMON
import com.ervin.mypokedex.utils.cancelNotification
import com.ervin.mypokedex.utils.getNotificationBuilder
import kotlinx.coroutines.*
import me.sargunvohra.lib.pokekotlin.client.PokeApiClient
import me.sargunvohra.lib.pokekotlin.model.Pokemon
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


class LaunchAppService : Service() {
    private var currentNotify = 0
    private val job = Job()

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val manager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotification()
        val pokeRepo = Injection.provideRepository(App())
        CoroutineScope(job + Dispatchers.IO).launch {
            try {
                val offset = intent.getIntExtra("offset", 0)
                val limit = intent.getIntExtra("limit", 0) - offset
                Log.d("tag", "$offset $limit")
                var count = offset
                val maxLimit = limit - (limit % 100)
                var i = 1
                while (count < maxLimit) {
                    manager.notify(ON_LOADING_POKEMON,updateNotificationForRemote(maxLimit/100, i))
                    getRemotePokemon(pokeRepo, count, 100)
                    count += 100
                    i++
                }
                val currLimit = limit % 100
                getRemotePokemon(pokeRepo, count, currLimit)
                sendFeedbackToActivity(true)
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

    private suspend fun getRemotePokemon(pokeRepo: PokemonRepository, offset: Int, limit: Int) {
        val retrievedList = pokeRepo.getRemotePokemonLimit(offset, limit)
        val pokeApi = PokeApiClient()
        val remotePokemonList: MutableList<Pokemon> = ArrayList()
        retrievedList.listResponseAPI.forEach { simplePokemonResponse ->
            val pokemonID = simplePokemonResponse.urlResponse.split("/")[6].toInt()
            Log.d("test", pokemonID.toString())
            remotePokemonList.add(pokeApi.getPokemon(pokemonID))
        }
        val pokemonEntities: MutableList<PokemonEntity> = ArrayList()
        val pokemonCompositeType: MutableList<PokemonTypeElementEntity> = ArrayList()

        remotePokemonList.forEach { pokemon ->

            //                    var imagePath: String? = ""
//                    Glide.with(App().getContext())
//                        .asBitmap()
//                        .load(pokemon.sprites.frontDefault)
//                        .diskCacheStrategy(DiskCacheStrategy.ALL)
//                        .into(object : CustomTarget<Bitmap>(96,96) {
//                            override fun onLoadCleared(placeholder: Drawable?) {
//                            }
//
//                            override fun onResourceReady(
//                                resource: Bitmap,
//                                transition: Transition<in Bitmap>?
//                            ) {
//                                imagePath = saveImage(resource)
//                            }
//                        }
            pokemonEntities.add(
                PokemonEntity(
                    pokemon.id,
                    pokemon.name,
//                            imagePath ?: ""
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
            )
            pokemon.types.forEach { pokemonType ->
                pokemonCompositeType.add(
                    PokemonTypeElementEntity(
                        pokemon.id,
                        pokemonType.type.id
                    )
                )
            }
        }
        pokeRepo.saveLocalPokemons(pokemonEntities, pokemonCompositeType)
    }

    private fun createNotification() {
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

    private fun updateNotificationForRemote(max:Int, progress:Int):Notification{
        val builder = getNotificationBuilder(
            ONGOING_CHANNEL_ID,
            NotificationManagerCompat.IMPORTANCE_HIGH
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

    private fun saveImage(image: Bitmap): String? {
        var savedImagePath: String? = null
        val imageFileName = "JPEG_" + "FILE_NAME" + ".jpg"
        val storageDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                .toString() + "/YOUR_FOLDER_NAME"
        )
        var success = true
        if (!storageDir.exists()) {
            success = storageDir.mkdirs()
        }
        if (success) {
            val imageFile = File(storageDir, imageFileName)
            savedImagePath = imageFile.getAbsolutePath()
            try {
                val fOut: OutputStream = FileOutputStream(imageFile)
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
                fOut.close()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                Log.d("imagesaved", e.message.toString())
            }
            // Add the image to the system gallery
            galleryAddPic(savedImagePath)
            Log.d("imagesaved", savedImagePath.toString())
        }
        return savedImagePath
    }

    private fun galleryAddPic(imagePath: String?) {
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val f = File(imagePath)
        val contentUri: Uri = Uri.fromFile(f)
        mediaScanIntent.data = contentUri
        sendBroadcast(mediaScanIntent)
    }


    companion object {
        private const val TAG = "TEST_SERVICE"
        const val RESULT_FETCHING_POKEMON = "RESULT_FETCH"
        const val INTENT_FILTER_SERVICE_GET_POKEMON = "INTENT_FILTER_GET_POKEMON"
    }


}