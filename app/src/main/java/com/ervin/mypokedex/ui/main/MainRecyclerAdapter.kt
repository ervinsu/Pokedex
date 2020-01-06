package com.ervin.mypokedex.ui.main


import android.content.Context
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ervin.mypokedex.data.model.SimplePokemonWithTypePojoModel

class MainRecyclerAdapter(val context: Context):PagedListAdapter<SimplePokemonWithTypePojoModel, RecyclerView.ViewHolder>(
    DATA_COMPARATOR
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MainViewHolder.create(parent, context)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val pokemon = getItem(position)?:return
        (holder as MainViewHolder).bind(pokemon)
    }

    companion object{
        private val DATA_COMPARATOR = object : DiffUtil.ItemCallback<SimplePokemonWithTypePojoModel>(){
            override fun areItemsTheSame(oldItem: SimplePokemonWithTypePojoModel, newItem: SimplePokemonWithTypePojoModel): Boolean =
                oldItem.pokemonModel.pokemonId == newItem.pokemonModel.pokemonId

            override fun areContentsTheSame(oldItem: SimplePokemonWithTypePojoModel, newItem: SimplePokemonWithTypePojoModel): Boolean =
                oldItem.pokemonModel == newItem.pokemonModel
        }
    }
}