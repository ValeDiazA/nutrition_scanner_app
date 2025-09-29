package com.nutritionscanner.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nutritionscanner.domain.model.*

class DatabaseConverters {
    private val gson = Gson()
    
    @TypeConverter
    fun fromIngredientList(ingredients: List<Ingredient>): String {
        return gson.toJson(ingredients)
    }
    
    @TypeConverter
    fun toIngredientList(ingredientsJson: String): List<Ingredient> {
        val type = object : TypeToken<List<Ingredient>>() {}.type
        return gson.fromJson(ingredientsJson, type)
    }
    
    @TypeConverter
    fun fromNutritionalInfo(nutritionalInfo: NutritionalInfo?): String? {
        return nutritionalInfo?.let { gson.toJson(it) }
    }
    
    @TypeConverter
    fun toNutritionalInfo(nutritionalInfoJson: String?): NutritionalInfo? {
        return nutritionalInfoJson?.let { gson.fromJson(it, NutritionalInfo::class.java) }
    }
    
    @TypeConverter
    fun fromNutritionalScore(score: NutritionalScore): String {
        return gson.toJson(score)
    }
    
    @TypeConverter
    fun toNutritionalScore(scoreJson: String): NutritionalScore {
        return gson.fromJson(scoreJson, NutritionalScore::class.java)
    }
    
    @TypeConverter
    fun fromLatamLabeling(latamLabeling: LatamLabeling?): String? {
        return latamLabeling?.let { gson.toJson(it) }
    }
    
    @TypeConverter
    fun toLatamLabeling(latamLabelingJson: String?): LatamLabeling? {
        return latamLabelingJson?.let { gson.fromJson(it, LatamLabeling::class.java) }
    }
    
    @TypeConverter
    fun fromAchievementList(achievements: List<Achievement>): String {
        return gson.toJson(achievements)
    }
    
    @TypeConverter
    fun toAchievementList(achievementsJson: String): List<Achievement> {
        val type = object : TypeToken<List<Achievement>>() {}.type
        return gson.fromJson(achievementsJson, type)
    }
    
    @TypeConverter
    fun fromUserPreferences(preferences: UserPreferences): String {
        return gson.toJson(preferences)
    }
    
    @TypeConverter
    fun toUserPreferences(preferencesJson: String): UserPreferences {
        return gson.fromJson(preferencesJson, UserPreferences::class.java)
    }
}