package com.example.data.response


import com.example.domain.Pokemon
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.parceler.Parcel

@Parcel
data class PokemonListResponse(
    @SerializedName("results") @Expose
    var results: ArrayList<Pokemon> = arrayListOf(),

    @SerializedName("next")
@Expose
var next: String? = null,
)