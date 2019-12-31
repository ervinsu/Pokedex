package com.ervin.mypokedex.ui.detail

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ervin.mypokedex.App
import com.ervin.mypokedex.R
import kotlinx.android.synthetic.main.type_view_item.view.*

class DetailTypeAdapter(private val listType: List<PokemonTypeWeaknessFrom>): RecyclerView.Adapter<DetailTypeAdapter.DetailViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        return DetailViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.type_view_item,parent,false))
    }

    override fun getItemCount(): Int {
        return listType.size
    }

    @SuppressLint("SetTextI18n", "DefaultLocale")
    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        val curType = listType[position]
        when (curType.typeAttack) {
            0 -> holder.itemView.tv_multiply_effective.text = "2x"
            1 -> holder.itemView.tv_multiply_effective.text = "0.5x"
            2 -> holder.itemView.tv_multiply_effective.text = "0.25x"
        }
        holder.itemView.tv_type_name.text = curType.typeElementWeaknessFrom.capitalize()
        val gradient = GradientDrawable().apply {
            setColor(Color.parseColor(curType.typeColor))
            cornerRadius = 8f
            setStroke(3, ContextCompat.getColor(App().getContext(), R.color.darkGrey))
        }
        holder.itemView.background = gradient
    }

    class DetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    data class PokemonTypeWeaknessFrom(
        val typeAttack: Int,
        val typeElementWeaknessFrom: String,
        val typeColor: String
    )
}