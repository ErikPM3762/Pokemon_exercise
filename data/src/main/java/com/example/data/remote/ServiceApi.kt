package com.example.data.remote

import com.example.data.response.PokemonListResponse
import com.example.domain.PokemonDetail
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ServiceApi {

    @GET("pokemon/")
    suspend fun getListPokemon(@Query("limit") limit: Int, @Query("offset") offset: Int): Response<PokemonListResponse>

    @GET("pokemon/{id}/")
    suspend fun getPokemonDetail(@Path("id") id: Int): Response<PokemonDetail>
}