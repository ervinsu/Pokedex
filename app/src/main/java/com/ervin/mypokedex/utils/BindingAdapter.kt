package com.ervin.mypokedex.utils

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("loadImage")
fun ImageView.loadImage( url:String){
    Glide.with(this.context)
        .load(url)
        .into(this)
}

@BindingAdapter("isVisible")
fun View.isVisible(boolean: Boolean){
    if(boolean)
        setShow()
    else
        setGone()
}