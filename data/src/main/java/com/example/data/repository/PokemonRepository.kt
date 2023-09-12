package com.example.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.RoomDatabase
import com.example.data.dataSources.PokemonRemoteDataSource
import com.example.data.local.PokemonDB
import com.example.data.local.PokemonEntity
import com.example.data.local.dao.PokemonDao
import com.example.data.response.PokemonListResponse
import com.example.data.utils.NetResult
import com.example.data.utils.loading
import com.example.domain.Pokemon
import com.example.domain.PokemonDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import javax.inject.Inject


class PokemonRepository @Inject constructor(
    private val remoteDataSource: PokemonRemoteDataSource,
    private val pokemonDao: PokemonDao
) {
    private var pokemonList: PokemonListResponse? = null
    private val databaseMutex = Mutex()


    suspend fun getListPokemon(offset: Int): Flow<NetResult<PokemonListResponse>> =
        remoteDataSource.getListPokemon(offset).loading().map { result ->
            if (result is NetResult.Success) {
                pokemonList = result.data
            }
            result
        }

    suspend fun getPokemonData(url: String): Flow<NetResult<PokemonDetail>> =
        remoteDataSource.getPokemonDetailByUrl(url).loading().map { result ->
            if (result is NetResult.Success) {
                val pokemonDetail = result.data
                val existingPokemon = withContext(Dispatchers.IO) {
                    databaseMutex.withLock {
                        pokemonDao.getPokemonById(pokemonDetail.id)
                    }
                }
                if (existingPokemon == null) {
                    savePokemonDetailToDatabase(pokemonDetail)
                }
            }
            result
        }



    private suspend fun savePokemonDetailToDatabase(pokemonDetail: PokemonDetail?) {
        if (pokemonDetail != null) {
            withContext(Dispatchers.IO) {
                val pokemonEntity = pokemonDetail.toPokemonEntity()
                pokemonDao.saveAllPokemon(pokemonEntity)
            }
        }
    }

    fun getPokemonList(): LiveData<List<PokemonEntity>> = pokemonDao.getPokemonByPosition()

    suspend fun getPokemonListOffline(): List<PokemonEntity> =
        withContext(Dispatchers.IO) {pokemonDao.getPokemonOffline()}

    suspend fun getPokemonById(id: Int): PokemonEntity? =
        withContext(Dispatchers.IO) {pokemonDao.getPokemonById(id)}

    suspend fun updatePokemon(pokemon: PokemonEntity): Int =
        withContext(Dispatchers.IO) {pokemonDao.updatePokemon(pokemon)}
    }



    fun PokemonDetail.toPokemonEntity(): PokemonEntity {
        return PokemonEntity(
            pokemonId = id,
            nombre = name,
            sprites = sprites.front_default,
            altura = height,
            peso = weight,
            tipo = types.map { it.type.name } as ArrayList<String>)
    }
