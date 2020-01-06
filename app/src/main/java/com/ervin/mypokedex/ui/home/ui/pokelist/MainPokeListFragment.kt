package com.ervin.mypokedex.ui.home.ui.pokelist

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ervin.mypokedex.R
import com.ervin.mypokedex.service.LaunchAppService
import com.ervin.mypokedex.ui.main.MainRecyclerAdapter
import com.ervin.mypokedex.ui.main.MainViewModel
import com.ervin.mypokedex.utils.*
import com.ervin.mypokedex.viewmodel.ViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainPokeListFragment : Fragment() {
    private lateinit var adapter: MainRecyclerAdapter
    private lateinit var job: Job
    private lateinit var root: View
    private var mLayoutState: Parcelable? = null

    companion object{
        const val TESTT="TEST"
        const val SAVED_KEY_PAGED_LIST = "PAGED_LIST"
        const val SAVED_INSTANCE_POSITION = "INSTANCE_POSITION"
    }

    private val viewModel: MainViewModel by lazy {
        val factory: ViewModelFactory =
            ViewModelFactory.getInstance(this@MainPokeListFragment.activity!!.application)
        return@lazy ViewModelProvider(
            this@MainPokeListFragment,
            factory
        ).get(MainViewModel::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.e("tessaaa", "onactivityCreated")
        if (savedInstanceState != null){
            Log.e("tessss", "onactivityCreated")
            mLayoutState = savedInstanceState.getParcelable(SAVED_INSTANCE_POSITION)!!
        }

        viewModel.apply {
            try {
                getLocalPokemon("").observe(
                    viewLifecycleOwner,
                    Observer { returnedValue ->
                        val sizeData = returnedValue.data?.size
                        Log.d("test11", "${sizeData.toString()} ")
                        if (sizeData != 0) {
                            adapter.submitList(returnedValue.data)
                            mLayoutState?.let {
                                root.rv_list_main.layoutManager!!.onRestoreInstanceState(mLayoutState)
                                mLayoutState = null
                            }
                            adapter.notifyDataSetChanged()
                            root.tv_magic_layout.setGone()
                            root.pg_loading.setGone()
                            root.pg_next_loading.setGone()
                            Log.d(
                                "test22", "${sizeData.toString()} ${isServiceRunning(
                                    activity!!,
                                    LaunchAppService::class.java
                                )}"
                            )
                        } else {
                            getIsDataLoaded().observe(viewLifecycleOwner, Observer {
                                if (!it) {
                                    setIsDataLoaded(true)
                                    CoroutineScope(job + Dispatchers.Main).launch {
                                        getCountRemotePokemon()

                                        loadRemotePokemons2().observe(
                                            viewLifecycleOwner,
                                            Observer { status ->
                                                Log.d("remotePokemon", status.toString())
                                                if (!status) {
                                                    val feedback = Snackbar.make(
                                                        activity!!.window.decorView,
                                                        "No Internet Connection",
                                                        Snackbar.LENGTH_LONG
                                                    )
                                                    feedback.setAction("Try Again") { viewModel.loadRemoteTypesPokemon() }
                                                    feedback.show()
                                                } else {
                                                    Snackbar.make(
                                                        activity!!.window.decorView,
                                                        "Database List start to be added",
                                                        Snackbar.LENGTH_LONG
                                                    ).show()
                                                }

                                            })

                                        loadRemoteTypesPokemon().collect { status ->
                                            Log.d("remoteTypes", status.toString())
                                            if (!status) {
                                                val feedback = Snackbar.make(
                                                    activity!!.window.decorView,
                                                    "No Internet Connection",
                                                    Snackbar.LENGTH_LONG
                                                )
                                                feedback.setAction("Try Again") { viewModel.loadRemoteTypesPokemon() }
                                                feedback.show()
                                            } else {
                                                Snackbar.make(
                                                    activity!!.window.decorView,
                                                    "Database TypePokemon start to be add",
                                                    Snackbar.LENGTH_LONG
                                                ).show()
                                            }
                                        }


                                        //handling result fetching
                                        getFetchBoolean().observe(
                                            viewLifecycleOwner,
                                            Observer { status ->
                                                if (status)
                                                    Snackbar.make(
                                                        activity!!.window.decorView,
                                                        "Update data Pokemon Success",
                                                        Snackbar.LENGTH_LONG
                                                    ).show()
                                                else if (!status) {
                                                    root.tv_magic_layout.text =
                                                        getString(R.string.No_Connection)
                                                    root.pg_loading.setGone()
                                                    val feedback = Snackbar.make(
                                                        activity!!.window.decorView,
                                                        "Failed to update data Pokemon",
                                                        Snackbar.LENGTH_LONG
                                                    )
                                                    feedback.setAction("Try Again") {
                                                        viewModel.loadRemotePokemons2()
                                                        viewModel.loadRemoteTypesPokemon()
                                                    }
                                                    feedback.show()
                                                }
                                            })


                                    }
                                }
                            })
                        }
                    })
            } catch (e: Exception) {
                Log.d("viewModelError", e.message.toString())
            }

            root.rv_list_main.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (!recyclerView.canScrollVertically(1)) {
                        if (isServiceRunning(activity!!, LaunchAppService::class.java)) {
                            root.pg_next_loading.setShow()
                            Log.d("pg_main", "show")
                        }
                    } else if (recyclerView.canScrollVertically(1)) {
                        root.pg_next_loading.setGone()
                        Log.d("pg_main", "gone")
                    }
                }
            })
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.apply {
            if (rootView.value == null) {
                rootView.value = inflater.inflate(R.layout.fragment_main_poke_list, container, false)
                Log.e("tess", "asdasd")
            }
            root = rootView.value!!
            job = Job()
            adapter = MainRecyclerAdapter(activity!!)
            (activity as AppCompatActivity).supportActionBar?.title = "MyPokedex"
            initAdapter()
            setHasOptionsMenu(true)
        }
        return root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.e("tessqwweq", "onsaveInsatance")
        outState.putParcelable(SAVED_INSTANCE_POSITION,root.rv_list_main.layoutManager!!.onSaveInstanceState())
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("tesss", "ondESTROy")
        job.cancel()
    }

    override fun onDetach() {
        super.onDetach()
        Log.e("tesss", "ondetach")
    }



    override fun onPause() {
       super.onPause()
        Log.e("tesss", "onPause")
        mLayoutState = root.rv_list_main.layoutManager!!.onSaveInstanceState()!!
    }

    override fun onResume() {
        super.onResume()
        Log.e("tesss", "onresume")
    }

    private fun initAdapter() {
        root.rv_list_main.adapter = adapter

        val rowSpan = Utils.calculateNoOfColumn(
            activity!!.applicationContext,
            resources.getDimensionPixelSize(R.dimen.width_row) / resources.displayMetrics.scaledDensity
        )
        val layoutManager = GridLayoutManager(activity?.applicationContext, rowSpan)
        root.rv_list_main.layoutManager = layoutManager
    }

    @SuppressLint("ObsoleteSdkInt")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            val manager = activity!!.getSystemService(Context.SEARCH_SERVICE) as SearchManager

            val search: SearchView = menu.findItem(R.id.app_bar_search)?.actionView as SearchView

            search.setSearchableInfo(manager.getSearchableInfo(activity!!.componentName))

            search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    activity!!.hideKeyboard()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    viewModel.apply {
                        getLocalPokemon(newText.toString()).observe(
                            this@MainPokeListFragment,
                            Observer { returnedValue ->
                                val sizeData = returnedValue.data?.size
                                Log.d("test22", "${sizeData.toString()} ")
                                if (sizeData != 0) {
                                    adapter.submitList(returnedValue.data)
                                    adapter.notifyDataSetChanged()
                                    root.tv_magic_layout.setGone()
                                }
                            })
                    }
                    return true
                }
            })
        }
        super.onCreateOptionsMenu(menu, inflater)
    }



}