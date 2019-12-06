package com.ervin.mypokedex.ui.main

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ervin.mypokedex.R
import com.ervin.mypokedex.data.model.SimplePokemonWithTypePojoModel
import com.ervin.mypokedex.ui.detail.DetailActivity


class MainViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val name: TextView = view.findViewById(R.id.pokeName)
    private val picture: ImageView = view.findViewById(R.id.poke_picture)
    private val container: View = view.findViewById(R.id.container_main_item)
    private val pokeContainer: View = view.findViewById(R.id.poke_container)

    private var simplePokemonItemPojoModel: SimplePokemonWithTypePojoModel? = null

    init {
        view.setOnClickListener {
            simplePokemonItemPojoModel?.pokemonModel?.let { pokemon ->
                val intent = Intent(view.context, DetailActivity::class.java)
                val pPokePicture =  Pair.create<View,String>(picture,"pokePicture")
                val pPokeContainer =  Pair.create(pokeContainer,"pokeContainer")
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    view.context as Activity,
                    pPokeContainer,
                    pPokePicture
//                    Pair.create(picture,"pokePicture"),
//                    Pair.create(pokeContainer, "pokeContainer")
                )
                intent.putExtra("tes",pokemon.pokemonId)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                view.context.startActivity(intent,options.toBundle())
            }
        }
    }

    fun bind(simplePokemonItemPojoModel: SimplePokemonWithTypePojoModel?) {
        if (simplePokemonItemPojoModel == null) {
            val resources = itemView.resources
            name.text = resources.getString(R.string.unknown)
            picture.visibility = View.GONE
        } else {
            showRepoData(simplePokemonItemPojoModel)
        }
    }

    @SuppressLint("ResourceType")
    private fun showRepoData(simplePokemonItemPojoModel: SimplePokemonWithTypePojoModel) {
        this.simplePokemonItemPojoModel = simplePokemonItemPojoModel
        name.text = simplePokemonItemPojoModel.pokemonModel.pokemonName
        Glide.with(picture.context)
            .load(simplePokemonItemPojoModel.pokemonModel.pokemonSpritesUrl)
            .into(picture)
        val arrayColorTypes = IntArray(2)
        for (i in simplePokemonItemPojoModel.typeList.indices) {
            arrayColorTypes[i] = Color.parseColor(simplePokemonItemPojoModel.typeList[i].typeColor)
            if (simplePokemonItemPojoModel.typeList.size == 1){
                arrayColorTypes[1] = Color.parseColor(simplePokemonItemPojoModel.typeList[0].typeColor)
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