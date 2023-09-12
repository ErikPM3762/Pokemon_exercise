package com.example.toolsandresources

import android.graphics.Color
import android.util.Log
import android.view.View
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter

object BindingAdapter {

    @BindingAdapter("app:randomBackgroundColor")
    fun setRandomBackgroundColor(view: CardView, enabled: Boolean) {
        if (enabled) {
            val randomColor = getRandomColor()
            view.setBackgroundColor(randomColor)
        } else  Log.e("Randomcolor", "****")
    }
    private fun getRandomColor(): Int {
        val red = (0..255).random()
        val green = (0..255).random()
        val blue = (0..255).random()
        return Color.rgb(red, green, blue)
    }

}