package com.example.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon_data")
data class PokemonEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "pokemon_id")
    val pokemonId: Int = 0,
    @ColumnInfo(name = "name")
    val nombre: String = "",
    @ColumnInfo(name = "sprites")
    val sprites: String = "",
    @ColumnInfo(name = "height")
    val altura: Int = 0,
    @ColumnInfo(name = "weight")
    val peso: Int = 0,
    @ColumnInfo(name = "type")
    val tipo: ArrayList<String> = arrayListOf(),
    @ColumnInfo(name = "favorite")
    var favorite : Boolean = false

)

