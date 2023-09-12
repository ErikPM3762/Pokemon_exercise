package com.example.data.dataSources

import com.example.data.response.PokemonListResponse
import com.example.data.utils.NetResult
import com.example.domain.Pokemon
import com.example.domain.PokemonDetail
import com.example.domain.Sprites
import kotlinx.coroutines.flow.Flow

interface PokemonRemoteDataSource {
    suspend fun getListPokemon(offset: Int): Flow<NetResult<PokemonListResponse>>
    suspend fun getPokemonDetailByUrl(url: String): Flow<NetResult<PokemonDetail>>
}