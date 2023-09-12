package com.example.pokemonexercise.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.PokemonEntity
import com.example.data.repository.PokemonRepository
import com.example.pokemonexercise.ui.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private val _pokemonData = MutableLiveData<Event<PokemonEntity>>()
    val pokemonData: LiveData<Event<PokemonEntity>> get() = _pokemonData


    fun getPokemonDetail(id: Int) {
        viewModelScope.launch {
            val pokemon = repository.getPokemonById(id)
            _pokemonData.postValue(Event(pokemon!!))
        }
    }

    fun toggleFavorite(pokemonId: Int, isFavorite: Boolean) {
        viewModelScope.launch {
            val pokemon = repository.getPokemonById(pokemonId)
            if (pokemon != null) {
                val updatedPokemon = pokemon.copy(favorite = isFavorite)
                repository.updatePokemon(updatedPokemon)
            }
        }
    }

}