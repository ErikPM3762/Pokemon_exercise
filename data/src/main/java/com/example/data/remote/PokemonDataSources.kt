package com.example.data.remote

import com.example.data.dataSources.PokemonRemoteDataSource
import com.example.data.response.PokemonListResponse
import com.example.data.utils.parse
import com.example.data.utils.toNetworkResult
import com.example.data.utils.NetResult
import com.example.domain.PokemonDetail
import com.example.domain.Sprites
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PokemonDataSources @Inject constructor(private val serviceApi: ServiceApi) :
    PokemonRemoteDataSource {
    val limit = 25

    override suspend fun getListPokemon(offset: Int): Flow<NetResult<PokemonListResponse>> = flow {
        emit(serviceApi.getListPokemon(limit, offset))
    }.catch { error -> emit(error.toNetworkResult()) }
        .map { res -> res.parse { it } }
        .flowOn(Dispatchers.IO)


    override suspend fun getPokemonDetailByUrl(url: String): Flow<NetResult<PokemonDetail>> = flow {
        emit(serviceApi.getPokemonDetail(extractPokemonIdFromUrl(url)))
    }.catch { error -> emit(error.toNetworkResult()) }
        .map { res -> res.parse {it} }
        .flowOn(Dispatchers.IO)

    private fun extractPokemonIdFromUrl(url: String): Int {
        val parts = url.split("/")
        return if (parts.size >= 3) {
            parts[parts.size - 2].toInt()
        } else {
            -1
        }
    }
}