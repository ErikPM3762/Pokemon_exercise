package com.example.pokemonexercise.ui.di

import android.content.Context
import com.example.data.local.PokemonDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DBModule {

    @Singleton
    @Provides
    fun providePokemonDatabase(@ApplicationContext context: Context): PokemonDB {
        return PokemonDB.create(context)
    }
}