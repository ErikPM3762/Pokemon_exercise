package com.example.pokemonexercise.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.PokemonEntity
import com.example.data.repository.PokemonRepository
import com.example.pokemonexercise.ui.utils.Event
import com.example.usecase.GetListPokemon
import com.example.data.utils.NetResult
import com.example.pokemonexercise.ui.home.adapters.AdapterPokemon
import com.example.usecase.GetPokemonDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.concurrent.LinkedBlockingQueue
import javax.inject.Inject

@HiltViewModel
class PokemonHomeViewModel @Inject constructor(
    private val getListPokemon: GetListPokemon,
    private val getPokemonDetail: GetPokemonDetail,
    private val repository: PokemonRepository
) : ViewModel() {

    var totalPokemonLoaded = 0
    val currentPokemon: LiveData<List<PokemonEntity>> = repository.getPokemonList()


    private val _successMore = MutableLiveData<Event<Boolean>>()
    val successMore: LiveData<Event<Boolean>> get() = _successMore

    private val _currentListOffline = MutableLiveData<List<PokemonEntity>>()
    val currentListOffline:LiveData<List<PokemonEntity>> get() = _currentListOffline

    private val _success = MutableLiveData<Event<Boolean>>()
    val success: LiveData<Event<Boolean>> get() = _success


    fun getListPokemon(offset: Int, more: Boolean) {
        viewModelScope.launch {
            getListPokemon.invoke(offset).collect { result ->
                if (result is NetResult.Success) {
                    val data = result.data.results
                    val totalRequests = data.size
                    var successfulResponses = 0

                    data.forEach { pokemon ->
                        val url = pokemon.url
                        getPokemonDetail.invoke(url).collect { resultData ->
                            if (resultData is NetResult.Success) {
                                successfulResponses++
                                if (successfulResponses == totalRequests) {
                                    totalPokemonLoaded += totalRequests
                                    if (more) {
                                        _successMore.postValue(Event(true))
                                    } else {
                                        _success.postValue(Event(true))
                                    }
                                }
                            }
                        }
                    }
                }
            }
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

    fun chargeDB() {
        _success.postValue(Event(true))
    }

    fun getAllData() {
        viewModelScope.launch {
            val pokemonList = repository.getPokemonListOffline()
            _currentListOffline.postValue(pokemonList)
        }
    }
}