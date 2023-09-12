package com.example.usecase

import com.example.data.repository.PokemonRepository
import com.example.data.response.PokemonListResponse
import com.example.data.utils.NetResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetListPokemon @Inject constructor (private val repository: PokemonRepository){
    suspend fun invoke(offset: Int) : Flow<NetResult<PokemonListResponse>> =
        repository.getListPokemon(offset)
}