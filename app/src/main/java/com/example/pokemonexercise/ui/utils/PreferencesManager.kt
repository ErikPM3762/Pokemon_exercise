package com.example.pokemonexercise.ui.utils

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager (context: Context){
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("PokemonApp", Context.MODE_PRIVATE)

    companion object {
        const val EMPTY_DATA_BASE = "emptyDB"
    }

    fun saveBoolean(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    fun clearPersonalInfoPreferences() {
        sharedPreferences.edit().remove(EMPTY_DATA_BASE)
            .apply()
    }

}