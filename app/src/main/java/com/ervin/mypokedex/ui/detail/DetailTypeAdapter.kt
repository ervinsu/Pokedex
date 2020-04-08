package com.ervin.mypokedex.ui.detail

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ervin.mypokedex.R
import kotlinx.android.synthetic.main.type_view_item.view.*

class DetailTypeAdapter(private val listType: List<PokemonTypeWeaknessFrom>, private val context:Context): RecyclerView.Adapter<DetailTypeAdapter.DetailViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        return DetailViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.type_view_item,parent,false))
    }

    override fun getItemCount(): Int {
        return listType.size
    }

    @SuppressLint("SetTextI18n", "DefaultLocale")
    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        val currType = listType[position]
        holder.itemView.tv_multiply_effective.text = "${currType.typeElementWeaknessMultiply} x"
        holder.itemView.tv_type_name.text = currType.typeName?.capitalize()
        val gradient = GradientDrawable().apply {
            setColor(Color.parseColor(currType.typeColor))
            cornerRadius = 8f
            setStroke(3, ContextCompat.getColor(context, R.color.detailDark))
        }
        holder.itemView.background = gradient
    }

    class DetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    data class PokemonTypeWeaknessFrom(
        val typeElementWeaknessMultiply: Float,
        val typeColor: String,
        var typeName : String? = ""
    )
}