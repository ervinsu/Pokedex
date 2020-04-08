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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ervin.mypokedex.R
import com.ervin.mypokedex.service.LaunchAppService
import com.ervin.mypokedex.ui.main.MainRecyclerAdapter
import com.ervin.mypokedex.ui.main.MainViewModel
import com.ervin.mypokedex.utils.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainPokeListFragment : Fragment() {
    private lateinit var adapter: MainRecyclerAdapter
    private lateinit var job: Job
    private lateinit var root: View
    private var mLayoutState: Parcelable? = null

    companion object{
        const val SAVED_INSTANCE_POSITION = "INSTANCE_POSITION"
        const val TAG = "MainPokeFragment"
    }

//    private val viewModel: MainViewModel by lazy {
//        val factory: ViewModelFactory =
//            ViewModelFactory.getInstance(this@MainPokeListFragment.activity!!.application)
//        return@lazy ViewModelProvider(
//            this@MainPokeListFragment,
//            factory
//        ).get(MainViewModel::class.java)
//    }
    private val viewModel by viewModel<MainViewModel>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        Log.e(TAG, "onactivityCreated")
        if (savedInstanceState != null){
            Log.e(TAG, "onactivityCreated")
            mLayoutState = savedInstanceState.getParcelable(SAVED_INSTANCE_POSITION)!!
        }

        viewModel.apply {
            try {
                getLocalPokemon("").observe(
                    viewLifecycleOwner,
                    Observer { returnedValue ->
                        val sizeData = returnedValue.data?.size
                        Log.d(TAG, " pokemon size ${sizeData.toString()} ")
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
                                TAG, "size pokemon now and isService running${sizeData.toString()} ${isServiceRunning(
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
                                                Log.d(TAG, "status loadData $status")
                                                if (!status) {
                                                    val feedback = Snackbar.make(
                                                        requireActivity().window.decorView,
                                                        "No Internet Connection",
                                                        Snackbar.LENGTH_LONG
                                                    )
                                                    //re-load called from viewmodel
                                                    feedback.setAction("Try Again") { loadRemoteTypesPokemon() }
                                                    feedback.show()
                                                } else {
                                                    Snackbar.make(
                                                        requireActivity().window.decorView,
                                                        "Database List start to be added",
                                                        Snackbar.LENGTH_LONG
                                                    ).show()
                                                }

                                            })

                                        loadRemoteTypesPokemon().collect { status ->
                                            Log.d(TAG, "remote type status $status")
                                            if (!status) {
                                                val feedback = Snackbar.make(
                                                    requireActivity().window.decorView,
                                                    "No Internet Connection",
                                                    Snackbar.LENGTH_LONG
                                                )
                                                //re-load called from viewmodel
                                                feedback.setAction("Try Again") { loadRemoteTypesPokemon() }
                                                feedback.show()
                                            } else {
                                                Snackbar.make(
                                                    requireActivity().window.decorView,
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
                                                        requireActivity().window.decorView,
                                                        "Update data Pokemon Success",
                                                        Snackbar.LENGTH_LONG
                                                    ).show()
                                                else if (!status) {
                                                    root.tv_magic_layout.text =
                                                        getString(R.string.No_Connection)
                                                    root.pg_loading.setGone()
                                                    val feedback = Snackbar.make(
                                                        requireActivity().window.decorView,
                                                        "Failed to update data Pokemon",
                                                        Snackbar.LENGTH_LONG
                                                    )

                                                    //reload fetch pokemon and types called from viewmodel
                                                    feedback.setAction("Try Again") {
                                                        loadRemotePokemons2()
                                                        loadRemoteTypesPokemon()
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
                        if (isServiceRunning(LaunchAppService::class.java)) {
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
            }
            root = rootView.value!!
            job = Job()
            adapter = MainRecyclerAdapter(requireActivity())
            (activity as AppCompatActivity).supportActionBar?.title = "MyPokedex"
            initAdapter()
            setHasOptionsMenu(true)
        }
        return root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.e(TAG, "onsaveInsatance")
        try {
            outState.putParcelable(
                SAVED_INSTANCE_POSITION,
                root.rv_list_main.layoutManager!!.onSaveInstanceState()
            )
        }catch (e:Exception){
            Log.d("expLayoutNotGenerated", e.message.toString())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(TAG, "ondESTROy")
        job.cancel()
    }

    override fun onDetach() {
        super.onDetach()
        Log.e(TAG, "ondetach")
    }



    override fun onPause() {
       super.onPause()
        Log.e(TAG, "onPause")
        mLayoutState = root.rv_list_main.layoutManager!!.onSaveInstanceState()!!
    }

    override fun onResume() {
        super.onResume()
        Log.e(TAG, "onresume")
    }

    private fun initAdapter() {
        root.rv_list_main.adapter = adapter

        val rowSpan = Utils.calculateNoOfColumn(
            requireActivity().applicationContext,
            resources.getDimensionPixelSize(R.dimen.width_row) / resources.displayMetrics.scaledDensity
        )
        val layoutManager = GridLayoutManager(activity?.applicationContext, rowSpan)
        root.rv_list_main.layoutManager = layoutManager
    }

    @SuppressLint("ObsoleteSdkInt")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            val manager = requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager

            val search: SearchView = menu.findItem(R.id.app_bar_search)?.actionView as SearchView

            search.setSearchableInfo(manager.getSearchableInfo(requireActivity().componentName))

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
                                Log.d(TAG, "size pokemon that fit with search ${sizeData.toString()} ")
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