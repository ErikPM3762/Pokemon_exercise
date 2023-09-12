package com.example.domain

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PokemonDetail(
    @SerializedName("sprites") @Expose
    var sprites: Sprites,
    var abilities: ArrayList<Abilities>,
    var types: ArrayList<Types>,
    var name: String,
    var id: Int,
    var height: Int,
    var weight: Int


)
