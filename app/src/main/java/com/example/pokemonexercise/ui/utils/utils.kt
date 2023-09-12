package com.example.pokemonexercise.ui.utils

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.toolsAndResources.R

fun generateInitials(name: String): String {
    val words = name.split(" ")

    if (words.isEmpty()) {
        return ""
    }

    var initials = ""
    for (word in words) {
        if (word.isNotEmpty()) {
            val firstChar = word[0]
            if (firstChar.isLetter() && firstChar.isUpperCase()) {
                initials += firstChar
            } else {
                return ""
            }
        }
    }

    return initials
}

fun ImageView.loadImageOrSetInitials(
    imageUrl: String?,
    initials: String,
    context: Context,
    backgroundResId: Int,
    textColorResId: Int
) {
    if (imageUrl != null && imageUrl.isNotEmpty()) {
        val requestOptions = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.usuario)
            .error(R.drawable.usuario)

        Glide.with(context)
            .load(imageUrl)
            .apply(requestOptions)
            .into(this)
    } else {
        if (initials.isNotEmpty()) {
            this.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.background_initials
                )
            )
            this.setBackgroundResource(backgroundResId)
            val textColor = ContextCompat.getColor(context, textColorResId)
            if (this is ImageView) {
                this.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        backgroundResId
                    )
                )
                this.setBackgroundResource(backgroundResId)
            } else if (this is View) {
                val txtImage = this.findViewById<TextView>(R.id.initialsText_Detail)
                txtImage.setTextColor(textColor)
                txtImage.visibility = View.VISIBLE
                txtImage.text = initials
            }
            this.setImageResource(R.drawable.usuario)
        }
    }
}

fun Activity.isConnected(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (connectivityManager != null) {
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
    }
    return false
}







