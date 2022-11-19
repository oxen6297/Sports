package com.example.sportscommunity.Adapter

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

object BindingImageAdapter{

    @BindingAdapter("imageUrl","error")
    @JvmStatic
    fun loadImage(imageView: ImageView, url: String, error: Drawable) {

        Glide.with(imageView.context).load(url).fitCenter().error(error).into(imageView)
    }
}