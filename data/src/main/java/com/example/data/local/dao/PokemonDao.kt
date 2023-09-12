package com.example.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.*
import com.example.data.local.PokemonEntity
import com.example.domain.PokemonDetail

@Dao
interface PokemonDao {


    @Query("SELECT * FROM pokemon_data")
    fun getPokemonByPosition(): LiveData<List<PokemonEntity>>

    @Query("SELECT * FROM pokemon_data")
    fun getPokemonOffline(): List<PokemonEntity>

    @Query("SELECT * FROM pokemon_data WHERE pokemon_id = :id")
    fun getPokemonById(id: Int): PokemonEntity?



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveAllPokemon(pokemon: PokemonEntity)

    @Update
    fun updatePokemon(pokemon: PokemonEntity) : Int
}