package com.alvarobrivaro.coffee.data.makeCoffee

import android.content.Context
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson
import java.lang.reflect.Type

class RecipeRepository(private val context: Context) {
    private val gson = Gson()
    
    fun getRecipes(): List<Recipe> {
        val jsonString = context.assets.open("listOfRecipes.json").bufferedReader().use { it.readText() }
        val type: Type = object : TypeToken<Map<String, List<Recipe>>>() {}.type
        val data = gson.fromJson<Map<String, List<Recipe>>>(jsonString, type)
        return data["recipes"] ?: emptyList()
    }
} 