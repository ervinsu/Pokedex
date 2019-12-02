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
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*


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
                                            tv_magic_layout.text = "Connection not Available"
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

//                lifecycleScope.launch(Dispatchers.IO) {
//                    if(isInternetAvailable()){
//                        withContext(Dispatchers.Main) {
//                            //get and save type pokemon
//                            getTypesPokemon()
//
//                            //handling result fetching
//                            getFetchBoolean().observe(this@MainActivity, Observer {
//                                if (it)
//                                    Snackbar.make(
//                                        window.decorView,
//                                        "Update data Pokemon Success",
//                                        Snackbar.LENGTH_LONG
//                                    ).show()
//                                else if (!it) {
//                                    val feedback = Snackbar.make(
//                                        window.decorView,
//                                        "Failed to update data Pokemon",
//                                        Snackbar.LENGTH_LONG
//                                    )
//                                    feedback.setAction("Try Again") { mainViewModel.getCountPokemon() }
//                                    feedback.show()
//                                }
//                            })
//
//                            //fetching pokemon and save to room
//                            getTryFetchPokemons().observe(
//                                this@MainActivity,
//                                Observer { response ->
//                                    when (response.status) {
//                                        Status.LOADING -> {
//                                            Log.d("aaaaa", "Loading")
//                                        }
//                                        Status.ERROR -> {
//                                            Log.d("aaaaa", "error")
//                                        }
//                                        Status.SUCCESS -> {
//                                            try {
//                                                Log.d("aaaaa", "success")
//                                                adapter.submitList(response.data)
//                                                adapter.notifyDataSetChanged()
//                                                Log.d("aaaaa", "${response.data?.size}")
//                                            } catch (e: Exception) {
//                                                Log.d(
//                                                    "errrorFetchSuccess",
//                                                    "${e.message}"
//                                                )
//                                            }
//                                        }
//                                    }
//                                    lifecycleScope.launch(Dispatchers.Main) {
//                                        pg_loading.setGone()
//                                        tv_magic_layout.setGone()
//                                    }
//                                })
//                        }
//                    }else{
//                        withContext(Dispatchers.Main) {
//                            tv_magic_layout.text = "No Internet Connection"
//                            pg_loading.setGone()
//                            val feedback = Snackbar.make(
//                                window.decorView,
//                                "No Internet Connection",
//                                Snackbar.LENGTH_LONG
//                            )
//                            feedback.setAction("Try Again") { mainViewModel.getLocalPokemon() }
//                            feedback.show()
//                        }
//                    }
//                }
            }
        }catch (e:Exception){
            Log.d("tes",e.message.toString())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    fun String.toDate(dateFormat:String = "HH:mm:ss",timeZone : TimeZone = TimeZone.getTimeZone("UTC")):Date?{
        val parser = SimpleDateFormat(dateFormat, Locale.getDefault())
        parser.timeZone = timeZone
        return parser.parse(this)
    }

    fun Date.formatToString(dateFormat: String, timeZone: TimeZone = TimeZone.getDefault()):String{
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
        formatter.timeZone = timeZone
        return formatter.format(this)
    }

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
