package com.ervin.mypokedex.ui.main

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ervin.mypokedex.R
import com.ervin.mypokedex.data.model.SimplePokemonWithTypePojo


class MainViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    private val name: TextView = view.findViewById(R.id.pokeName)
    private val picture: ImageView = view.findViewById(R.id.pokePicture)
    private val container: View = view.findViewById(R.id.container_main_item)
    private val pokeContainer: View = view.findViewById(R.id.pokeContainer)

    private var simplePokemonItemPojo: SimplePokemonWithTypePojo? = null

    init {
        view.setOnClickListener {
            simplePokemonItemPojo?.pokemon?.pokemonId?.let { pokemonID ->
                //                val intent = Intent(this, )
//                view.context.startActivity(intent)
            }
        }
    }

    fun bind(simplePokemonItemPojo: SimplePokemonWithTypePojo?) {
        if (simplePokemonItemPojo == null) {
            val resources = itemView.resources
            name.text = resources.getString(R.string.unknown)
            picture.visibility = View.GONE
        } else {
            showRepoData(simplePokemonItemPojo)
        }
    }

    @SuppressLint("ResourceType")
    private fun showRepoData(simplePokemonItemPojo: SimplePokemonWithTypePojo) {
        this.simplePokemonItemPojo = simplePokemonItemPojo
        name.text = simplePokemonItemPojo.pokemon.pokemonName
        Glide.with(picture.context)
            .load(simplePokemonItemPojo.pokemon.pokemonSpritesUrl)
            .into(picture)
        val arrayColorTypes = IntArray(2)
        for (i in simplePokemonItemPojo.typeList.indices) {
            arrayColorTypes[i] = Color.parseColor(simplePokemonItemPojo.typeList[i].typeColor)
            if (simplePokemonItemPojo.typeList.size == 1){
                arrayColorTypes[1] = Color.parseColor(simplePokemonItemPojo.typeList[0].typeColor)
            }
        }

        val gd = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, arrayColorTypes)
        gd.cornerRadius = 0f
        pokeContainer.background = gd

    }

    companion object {
        fun create(parent: ViewGroup): MainViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.main_view_item, parent, false)
            return MainViewHolder(view)
        }
    }
}