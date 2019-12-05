package com.ervin.mypokedex

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.ervin.mypokedex.utils.Utils
import com.ervin.mypokedex.utils.setGone
import com.ervin.mypokedex.viewmodel.ViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private val adapter = MainRecyclerAdapter()
    private lateinit var mainViewModel:MainViewModel
    private val job = Job()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        val time = "15:00:15"
//        Log.d("tes",time.toDate()?.formatToString("HH:mm:ss"))
        initAdapter()

        mainViewModel = obtainViewModel(this@MainActivity)
        try {
            mainViewModel.apply {
//                getPokemonType().observe(this@MainActivity, Observer {
//                    for(i in it.indices){
//                        Log.d("getPokemon","${it[i].typeEntity.typeName} ${it[i].typeSuperEffectiveEntity.size}")
//                    }
//                })

//                getSpecificPokemon().observe(this@MainActivity, Observer {
//                    Log.d("getPokemon", "${it.pokemon.pokemonName} ${it.typeElementPokemon.typeSuperEffectiveEntity.size}")
//                })

                //get saved pokemon
                getLocalPokemon().observe(this@MainActivity, Observer { returnedValue ->
                    val sizeData = returnedValue.data?.size
                    Log.d("test11", "${sizeData.toString()} ")
                    if (sizeData != 0) {
                        adapter.submitList(returnedValue.data)
                        adapter.notifyDataSetChanged()
                        tv_magic_layout.setGone()
                        pg_loading.setGone()
//                        CoroutineScope(job + Dispatchers.IO).launch {
//                            updateRemotePokemons().collect { status ->
//                                Log.d("remotePokemon", status.toString())
//                                if (!status) {
//                                    val feedback = Snackbar.make(
//                                        window.decorView,
//                                        "Cant Update Pokemon",
//                                        Snackbar.LENGTH_LONG
//                                    )
//                                    feedback.setAction("Try Again") { mainViewModel.loadRemoteTypesPokemon() }
//                                    feedback.show()
//                                } else {
//                                    Snackbar.make(
//                                        window.decorView,
//                                        "Data Updated!",
//                                        Snackbar.LENGTH_LONG
//                                    ).show()
//                                }
//                            }
//                        }
                    } else {
                        getIsDataLoaded().observe(this@MainActivity, Observer {
                            if(!it){
                                setIsDataLoaded(true)
                                CoroutineScope(job + Dispatchers.Main).launch{
                                    loadRemoteTypesPokemon().collect {status->
                                        Log.d("remoteTypes",status.toString())
                                        if (!status){
                                            val feedback = Snackbar.make(
                                                window.decorView,
                                                "No Internet Connection",
                                                Snackbar.LENGTH_LONG
                                            )
                                            feedback.setAction("Try Again") { mainViewModel.loadRemoteTypesPokemon() }
                                            feedback.show()
                                        }else{
                                            Snackbar.make(window.decorView, "Database TypePokemon Added",Snackbar.LENGTH_LONG).show()
                                        }
                                    }

                                    loadRemotePokemons().collect{status->
                                        Log.d("remotePokemon",status.toString())
                                        if (!status){
                                            val feedback = Snackbar.make(
                                                window.decorView,
                                                "No Internet Connection",
                                                Snackbar.LENGTH_LONG
                                            )
                                            feedback.setAction("Try Again") { mainViewModel.loadRemoteTypesPokemon() }
                                            feedback.show()
                                        }else{
                                            Snackbar.make(window.decorView, "Database List Added",Snackbar.LENGTH_LONG).show()
                                        }
                                    }

                                    //handling result fetching
                                    getFetchBoolean().observe(this@MainActivity, Observer {status->
                                        if (status)
                                            Snackbar.make(
                                                window.decorView,
                                                "Update data Pokemon Success",
                                                Snackbar.LENGTH_LONG
                                            ).show()
                                        else if (!status) {
                                            tv_magic_layout.text = getString(R.string.No_Connection)
                                            pg_loading.setGone()
                                            val feedback = Snackbar.make(
                                                window.decorView,
                                                "Failed to update data Pokemon",
                                                Snackbar.LENGTH_LONG
                                            )
                                            feedback.setAction("Try Again") {
                                                mainViewModel.loadRemotePokemons()
                                                mainViewModel.loadRemoteTypesPokemon() }
                                            feedback.show()
                                        }
                                    })


                                }
                            }
                        })
                    }
                })
            }
        }catch (e:Exception){
            Log.d("tes",e.message.toString())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

//    fun String.toDate(dateFormat:String = "HH:mm:ss",timeZone : TimeZone = TimeZone.getTimeZone("UTC")):Date?{
//        val parser = SimpleDateFormat(dateFormat, Locale.getDefault())
//        parser.timeZone = timeZone
//        return parser.parse(this)
//    }
//
//    fun Date.formatToString(dateFormat: String, timeZone: TimeZone = TimeZone.getDefault()):String{
//        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
//        formatter.timeZone = timeZone
//        return formatter.format(this)
//    }

    private fun initAdapter() {
        rv_list_main.adapter = adapter

        val rowSpan = Utils.calculateNoOfColumn(
            applicationContext,
            resources.getDimensionPixelSize(R.dimen.width_row) / resources.displayMetrics.scaledDensity
        )
        val layoutManager = GridLayoutManager(this, rowSpan)
        rv_list_main.layoutManager = layoutManager
    }


    private fun obtainViewModel(activity: MainActivity):MainViewModel{
        val factory: ViewModelFactory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(MainViewModel::class.java)
    }
}
