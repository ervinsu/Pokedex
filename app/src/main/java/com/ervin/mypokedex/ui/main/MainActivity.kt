package com.ervin.mypokedex.ui.main

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ervin.mypokedex.R
import com.ervin.mypokedex.service.LaunchAppService
import com.ervin.mypokedex.utils.*
import com.ervin.mypokedex.viewmodel.ViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private val adapter = MainRecyclerAdapter(this@MainActivity)
    private lateinit var mainViewModel: MainViewModel
    private val job = Job()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(main_toolbar)
        supportActionBar?.title = "MyPokedex"
        initAdapter()

        mainViewModel = obtainViewModel(this@MainActivity)
        try {
            mainViewModel.apply {
                //get saved pokemon
                getLocalPokemon("").observe(this@MainActivity, Observer { returnedValue ->
                    val sizeData = returnedValue.data?.size
                    Log.d("test11", "${sizeData.toString()} ")
                    if (sizeData != 0) {
                        adapter.submitList(returnedValue.data)
                        adapter.notifyDataSetChanged()
                        tv_magic_layout.setGone()
                        pg_loading.setGone()
                        pg_next_loading.setGone()
                        Log.d("test22", "${sizeData.toString()} ${isServiceRunning(LaunchAppService::class.java)}")
                    } else {
                        getIsDataLoaded().observe(this@MainActivity, Observer {
                            if(!it){
                                setIsDataLoaded(true)
                                CoroutineScope(job + Dispatchers.Main).launch{
                                    getCountRemotePokemon()

                                    loadRemotePokemons2().observe(this@MainActivity, Observer {status->
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
                                            Snackbar.make(window.decorView, "Database List start to be added",Snackbar.LENGTH_LONG).show()
                                        }

                                    })

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
                                            Snackbar.make(window.decorView, "Database TypePokemon start to be add",Snackbar.LENGTH_LONG).show()
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
                                                mainViewModel.loadRemotePokemons2()
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


        rv_list_main.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    if(isServiceRunning(LaunchAppService::class.java)){
                        pg_next_loading.setShow()
                        Log.d("pg_main","show")
                    }
                }else if(recyclerView.canScrollVertically(1)){
                    pg_next_loading.setGone()
                    Log.d("pg_main","gone")
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
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

    @SuppressLint("ObsoleteSdkInt")
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            val manager = getSystemService(Context.SEARCH_SERVICE) as SearchManager

            val search: SearchView = menu?.findItem(R.id.app_bar_search)?.actionView as SearchView

            search.setSearchableInfo(manager.getSearchableInfo(componentName))

            search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    this@MainActivity.hideKeyboard()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    mainViewModel.apply {
                        getLocalPokemon(newText.toString()).observe(this@MainActivity, Observer { returnedValue ->
                            val sizeData = returnedValue.data?.size
                            Log.d("test22", "${sizeData.toString()} ")
                            if (sizeData != 0) {
                                adapter.submitList(returnedValue.data)
                                adapter.notifyDataSetChanged()
                                tv_magic_layout.setGone()
                            }
                        })
                    }
                    return true
                }
            })
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.app_bar_refresh_fetch_pokemon->{
                Snackbar.make(window.decorView, "Refresh Clicked",Snackbar.LENGTH_LONG).show()
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }


    private fun obtainViewModel(activity: MainActivity): MainViewModel {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(MainViewModel::class.java)
    }
}
