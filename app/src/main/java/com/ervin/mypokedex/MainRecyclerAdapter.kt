package com.ervin.mypokedex


import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ervin.mypokedex.data.model.SimplePokemonWithTypePojo
import com.ervin.mypokedex.ui.main.MainViewHolder

class MainRecyclerAdapter:PagedListAdapter<SimplePokemonWithTypePojo, RecyclerView.ViewHolder>(DATA_COMPARATOR) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MainViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val pokemon = getItem(position)
        if(pokemon!=null){
            (holder as MainViewHolder).bind(pokemon)
        }
    }

    companion object{
        private val DATA_COMPARATOR = object : DiffUtil.ItemCallback<SimplePokemonWithTypePojo>(){
            override fun areItemsTheSame(oldItem: SimplePokemonWithTypePojo, newItem: SimplePokemonWithTypePojo): Boolean =
                oldItem.pokemon.pokemonId == newItem.pokemon.pokemonId

            override fun areContentsTheSame(oldItem: SimplePokemonWithTypePojo, newItem: SimplePokemonWithTypePojo): Boolean =
                oldItem.pokemon == newItem.pokemon
        }
    }
}