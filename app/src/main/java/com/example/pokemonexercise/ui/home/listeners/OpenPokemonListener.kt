package com.example.pokemonexercise.ui.home.listeners


interface OpenPokemonListener {
    fun open(idPokemon: Int)

    fun openImageFull()

    fun saveFavorite(isFavorite: Boolean, idPokemon : Int)
}