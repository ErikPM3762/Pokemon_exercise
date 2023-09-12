package com.example.usecase

import com.example.data.repository.PokemonRepository
import com.example.data.utils.NetResult
import com.example.domain.PokemonDetail
import com.example.domain.Sprites
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPokemonDetail @Inject constructor (private val repository: PokemonRepository){
    suspend fun invoke(url:String) : Flow<NetResult<PokemonDetail>> =
        repository.getPokemonData(url)
}