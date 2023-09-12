package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.data.local.converters.ListStringConverter
import com.example.data.local.dao.PokemonDao


@Database(
    entities = [PokemonEntity::class],
    version = 2,
    exportSchema = false
)

@TypeConverters(ListStringConverter::class)
abstract class PokemonDB : RoomDatabase() {

    abstract val dao: PokemonDao

    companion object {
        private const val name = "pokemon.db"
        fun create(context: Context): PokemonDB {
            return Room.databaseBuilder(context, PokemonDB::class.java, name).build()
        }
    }
}