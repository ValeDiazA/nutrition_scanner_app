package com.nutritionscanner.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val id: String,
    val name: String,
    val brand: String,
    val barcode: String? = null,
    val ingredients: List<Ingredient>,
    val nutritionalInfo: NutritionalInfo,
    val score: NutritionalScore,
    val latamLabeling: LatamLabeling? = null,
    val scanTimestamp: Long = System.currentTimeMillis()
) : Parcelable

@Parcelize
data class Ingredient(
    val name: String,
    val originalText: String,
    val category: IngredientCategory,
    val riskLevel: RiskLevel,
    val synonyms: List<String> = emptyList(),
    val description: String? = null
) : Parcelable

enum class IngredientCategory {
    NATURAL,
    PROCESSED,
    ULTRA_PROCESSED,
    ADDITIVE,
    PRESERVATIVE,
    COLORANT,
    SWEETENER,
    HIDDEN_SUGAR,
    TRANS_FAT,
    UNKNOWN
}

enum class RiskLevel {
    LOW,
    MEDIUM,
    HIGH,
    CRITICAL
}

@Parcelize
data class NutritionalInfo(
    val calories: Double? = null,
    val totalFat: Double? = null,
    val saturatedFat: Double? = null,
    val transFat: Double? = null,
    val sodium: Double? = null,
    val totalCarbs: Double? = null,
    val sugar: Double? = null,
    val addedSugar: Double? = null,
    val protein: Double? = null,
    val fiber: Double? = null
) : Parcelable

@Parcelize
data class NutritionalScore(
    val overall: Float, // 0-100
    val trafficLight: TrafficLight,
    val breakdown: ScoreBreakdown,
    val ranking: Int? = null, // Position among similar products
    val totalProducts: Int? = null
) : Parcelable

enum class TrafficLight {
    GREEN,
    YELLOW,
    RED
}

@Parcelize
data class ScoreBreakdown(
    val naturalIngredients: Float,
    val processedScore: Float,
    val additivesScore: Float,
    val sugarScore: Float,
    val fatScore: Float,
    val sodiumScore: Float
) : Parcelable

@Parcelize
data class LatamLabeling(
    val country: String,
    val warnings: List<WarningSeal>
) : Parcelable

@Parcelize
data class WarningSeal(
    val type: SealType,
    val isPresent: Boolean
) : Parcelable

enum class SealType {
    EXCESS_CALORIES,
    EXCESS_SODIUM,
    EXCESS_SUGAR,
    EXCESS_SATURATED_FATS,
    CONTAINS_SWEETENERS,
    CONTAINS_CAFFEINE
}