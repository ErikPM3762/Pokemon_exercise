package com.example.data.local.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ListStringConverter {
    @TypeConverter
    fun fromString(value: String?): ArrayList<String>? {
        if (value == null) {
            return arrayListOf()
        }
        val listType = object : TypeToken<ArrayList<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: ArrayList<String>?): String? {
        return Gson().toJson(list)
    }
}