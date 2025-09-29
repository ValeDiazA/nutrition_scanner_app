package com.nutritionscanner.data.analysis

import com.nutritionscanner.domain.model.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.max
import kotlin.math.min

@Singleton
class NutritionalScoreCalculator @Inject constructor() {
    
    fun calculateScore(ingredients: List<Ingredient>, nutritionalInfo: NutritionalInfo? = null): NutritionalScore {
        val breakdown = calculateScoreBreakdown(ingredients, nutritionalInfo)
        val overall = calculateOverallScore(breakdown)
        val trafficLight = determineTrafficLight(overall)
        
        return NutritionalScore(
            overall = overall,
            trafficLight = trafficLight,
            breakdown = breakdown,
            ranking = null, // To be filled by ranking service
            totalProducts = null
        )
    }
    
    private fun calculateScoreBreakdown(
        ingredients: List<Ingredient>,
        nutritionalInfo: NutritionalInfo?
    ): ScoreBreakdown {
        val naturalScore = calculateNaturalIngredientsScore(ingredients)
        val processedScore = calculateProcessedScore(ingredients)
        val additivesScore = calculateAdditivesScore(ingredients)
        val sugarScore = calculateSugarScore(ingredients, nutritionalInfo)
        val fatScore = calculateFatScore(ingredients, nutritionalInfo)
        val sodiumScore = calculateSodiumScore(nutritionalInfo)
        
        return ScoreBreakdown(
            naturalIngredients = naturalScore,
            processedScore = processedScore,
            additivesScore = additivesScore,
            sugarScore = sugarScore,
            fatScore = fatScore,
            sodiumScore = sodiumScore
        )
    }
    
    private fun calculateNaturalIngredientsScore(ingredients: List<Ingredient>): Float {
        if (ingredients.isEmpty()) return 50f
        
        val naturalCount = ingredients.count { it.category == IngredientCategory.NATURAL }
        val percentage = naturalCount.toFloat() / ingredients.size
        
        return percentage * 100f
    }
    
    private fun calculateProcessedScore(ingredients: List<Ingredient>): Float {
        if (ingredients.isEmpty()) return 100f
        
        val ultraProcessedCount = ingredients.count { 
            it.category == IngredientCategory.ULTRA_PROCESSED 
        }
        val processedCount = ingredients.count { 
            it.category == IngredientCategory.PROCESSED 
        }
        
        val penaltyScore = (ultraProcessedCount * 15) + (processedCount * 8)
        return max(0f, 100f - penaltyScore)
    }
    
    private fun calculateAdditivesScore(ingredients: List<Ingredient>): Float {
        if (ingredients.isEmpty()) return 100f
        
        val riskPenalties = ingredients.sumOf { ingredient ->
            when (ingredient.riskLevel) {
                RiskLevel.CRITICAL -> 25
                RiskLevel.HIGH -> 15
                RiskLevel.MEDIUM -> 8
                RiskLevel.LOW -> 2
            }.toLong()
        }.toInt()
        
        return max(0f, 100f - riskPenalties.toFloat())
    }
    
    private fun calculateSugarScore(ingredients: List<Ingredient>, nutritionalInfo: NutritionalInfo?): Float {
        var score = 100f
        
        // Penalty for hidden sugars in ingredients
        val hiddenSugarCount = ingredients.count { it.category == IngredientCategory.HIDDEN_SUGAR }
        score -= hiddenSugarCount * 20f
        
        // Penalty based on actual sugar content if available
        nutritionalInfo?.let { info ->
            info.addedSugar?.let { addedSugar ->
                when {
                    addedSugar > 15 -> score -= 40f // Very high
                    addedSugar > 10 -> score -= 25f // High
                    addedSugar > 5 -> score -= 15f  // Medium
                }
            } ?: info.sugar?.let { totalSugar ->
                when {
                    totalSugar > 20 -> score -= 30f
                    totalSugar > 15 -> score -= 20f
                    totalSugar > 10 -> score -= 10f
                }
            }
        }
        
        return max(0f, score)
    }
    
    private fun calculateFatScore(ingredients: List<Ingredient>, nutritionalInfo: NutritionalInfo?): Float {
        var score = 100f
        
        // Severe penalty for trans fats
        val transFatCount = ingredients.count { it.category == IngredientCategory.TRANS_FAT }
        score -= transFatCount * 50f
        
        // Penalty based on actual fat content
        nutritionalInfo?.let { info ->
            info.transFat?.let { transFat ->
                if (transFat > 0) score -= 60f // Any trans fat is bad
            }
            
            info.saturatedFat?.let { satFat ->
                when {
                    satFat > 10 -> score -= 25f
                    satFat > 5 -> score -= 15f
                    satFat > 3 -> score -= 8f
                }
            }
        }
        
        return max(0f, score)
    }
    
    private fun calculateSodiumScore(nutritionalInfo: NutritionalInfo?): Float {
        var score = 100f
        
        nutritionalInfo?.sodium?.let { sodium ->
            when {
                sodium > 800 -> score -= 40f  // Very high
                sodium > 600 -> score -= 25f  // High
                sodium > 400 -> score -= 15f  // Medium high
                sodium > 200 -> score -= 8f   // Moderate
            }
        }
        
        return max(0f, score)
    }
    
    private fun calculateOverallScore(breakdown: ScoreBreakdown): Float {
        // Weighted average of all components
        val weights = mapOf(
            "natural" to 0.20f,
            "processed" to 0.20f,
            "additives" to 0.20f,
            "sugar" to 0.15f,
            "fat" to 0.15f,
            "sodium" to 0.10f
        )
        
        return (breakdown.naturalIngredients * weights["natural"]!! +
                breakdown.processedScore * weights["processed"]!! +
                breakdown.additivesScore * weights["additives"]!! +
                breakdown.sugarScore * weights["sugar"]!! +
                breakdown.fatScore * weights["fat"]!! +
                breakdown.sodiumScore * weights["sodium"]!!)
    }
    
    private fun determineTrafficLight(score: Float): TrafficLight {
        return when {
            score >= 70f -> TrafficLight.GREEN
            score >= 40f -> TrafficLight.YELLOW
            else -> TrafficLight.RED
        }
    }
    
    fun calculateLatamWarnings(
        ingredients: List<Ingredient>, 
        nutritionalInfo: NutritionalInfo?,
        country: String
    ): LatamLabeling {
        val warnings = mutableListOf<WarningSeal>()
        
        // These thresholds are based on Chilean, Mexican, and Argentinian regulations
        nutritionalInfo?.let { info ->
            // Excess calories (varies by country and serving size)
            warnings.add(WarningSeal(SealType.EXCESS_CALORIES, isExcessCalories(info, country)))
            
            // Excess sodium
            warnings.add(WarningSeal(SealType.EXCESS_SODIUM, isExcessSodium(info, country)))
            
            // Excess sugar
            warnings.add(WarningSeal(SealType.EXCESS_SUGAR, isExcessSugar(info, country)))
            
            // Excess saturated fats
            warnings.add(WarningSeal(SealType.EXCESS_SATURATED_FATS, isExcessSaturatedFats(info, country)))
        }
        
        // Contains sweeteners (based on ingredients)
        val containsSweeteners = ingredients.any { 
            it.category == IngredientCategory.SWEETENER ||
            it.name.lowercase().contains("aspartame") ||
            it.name.lowercase().contains("sucralosa") ||
            it.name.lowercase().contains("acesulfame")
        }
        warnings.add(WarningSeal(SealType.CONTAINS_SWEETENERS, containsSweeteners))
        
        // Contains caffeine
        val containsCaffeine = ingredients.any {
            it.name.lowercase().contains("cafeína") ||
            it.name.lowercase().contains("café") ||
            it.name.lowercase().contains("té")
        }
        warnings.add(WarningSeal(SealType.CONTAINS_CAFFEINE, containsCaffeine))
        
        return LatamLabeling(country, warnings)
    }
    
    private fun isExcessCalories(info: NutritionalInfo, country: String): Boolean {
        return info.calories?.let { it > 300 } ?: false // Simplified threshold
    }
    
    private fun isExcessSodium(info: NutritionalInfo, country: String): Boolean {
        return info.sodium?.let { 
            when (country) {
                "CL" -> it > 400  // Chile threshold
                "MX" -> it > 300  // Mexico threshold
                "AR" -> it > 500  // Argentina threshold
                else -> it > 400
            }
        } ?: false
    }
    
    private fun isExcessSugar(info: NutritionalInfo, country: String): Boolean {
        return info.sugar?.let {
            when (country) {
                "CL" -> it > 15
                "MX" -> it > 10
                "AR" -> it > 20
                else -> it > 15
            }
        } ?: false
    }
    
    private fun isExcessSaturatedFats(info: NutritionalInfo, country: String): Boolean {
        return info.saturatedFat?.let {
            when (country) {
                "CL" -> it > 6
                "MX" -> it > 4
                "AR" -> it > 8
                else -> it > 6
            }
        } ?: false
    }
}