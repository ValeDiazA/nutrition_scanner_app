package com.nutritionscanner.data.analysis

import com.nutritionscanner.domain.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IngredientAnalyzer @Inject constructor() {
    
    private val sugarSynonyms = setOf(
        "jarabe de maíz", "jarabe de maíz de alta fructosa", "dextrosa", 
        "fructosa", "glucosa", "maltosa", "sacarosa", "azúcar invertido",
        "sirope", "melaza", "miel de maíz", "jarabe de glucosa"
    )
    
    private val transFatKeywords = setOf(
        "aceite parcialmente hidrogenado", "grasa parcialmente hidrogenada",
        "aceite vegetal hidrogenado", "margarina", "shortening"
    )
    
    private val artificialColorants = setOf(
        "tartrazina", "amarillo 5", "amarillo 6", "rojo 40", "azul 1", "azul 2",
        "e102", "e110", "e129", "e132", "e133", "caramelo iv", "e150d"
    )
    
    private val preservatives = setOf(
        "benzoato de sodio", "sorbato de potasio", "ácido cítrico", "bht", "bha",
        "e211", "e202", "e330", "e321", "e320", "nitrito de sodio", "sulfito"
    )
    
    suspend fun analyzeIngredients(ingredientText: String): List<Ingredient> = withContext(Dispatchers.IO) {
        val cleanedText = cleanIngredientText(ingredientText)
        val ingredientNames = parseIngredients(cleanedText)
        
        ingredientNames.map { name ->
            val category = categorizeIngredient(name)
            val riskLevel = assessRiskLevel(name, category)
            val synonyms = findSynonyms(name)
            
            Ingredient(
                name = name.trim(),
                originalText = name,
                category = category,
                riskLevel = riskLevel,
                synonyms = synonyms,
                description = getIngredientDescription(name, category)
            )
        }
    }
    
    private fun cleanIngredientText(text: String): String {
        return text
            .replace(Regex("ingredientes?:?", RegexOption.IGNORE_CASE), "")
            .replace(Regex("contiene:?", RegexOption.IGNORE_CASE), "")
            .replace(Regex("\\([^)]*\\)"), "") // Remove parentheses content
            .replace(Regex("\\*+"), "") // Remove asterisks
            .trim()
    }
    
    private fun parseIngredients(text: String): List<String> {
        return text.split(Regex("[,;]"))
            .map { it.trim() }
            .filter { it.isNotEmpty() && it.length > 2 }
    }
    
    private fun categorizeIngredient(ingredient: String): IngredientCategory {
        val lowerIngredient = ingredient.lowercase()
        
        return when {
            isHiddenSugar(lowerIngredient) -> IngredientCategory.HIDDEN_SUGAR
            isTransFat(lowerIngredient) -> IngredientCategory.TRANS_FAT
            isArtificialColorant(lowerIngredient) -> IngredientCategory.COLORANT
            isPreservative(lowerIngredient) -> IngredientCategory.PRESERVATIVE
            isNaturalIngredient(lowerIngredient) -> IngredientCategory.NATURAL
            isUltraProcessed(lowerIngredient) -> IngredientCategory.ULTRA_PROCESSED
            isProcessed(lowerIngredient) -> IngredientCategory.PROCESSED
            isAdditive(lowerIngredient) -> IngredientCategory.ADDITIVE
            else -> IngredientCategory.UNKNOWN
        }
    }
    
    private fun assessRiskLevel(ingredient: String, category: IngredientCategory): RiskLevel {
        return when (category) {
            IngredientCategory.TRANS_FAT -> RiskLevel.CRITICAL
            IngredientCategory.HIDDEN_SUGAR -> RiskLevel.HIGH
            IngredientCategory.COLORANT -> RiskLevel.HIGH
            IngredientCategory.ULTRA_PROCESSED -> RiskLevel.HIGH
            IngredientCategory.PRESERVATIVE -> RiskLevel.MEDIUM
            IngredientCategory.ADDITIVE -> RiskLevel.MEDIUM
            IngredientCategory.PROCESSED -> RiskLevel.MEDIUM
            IngredientCategory.NATURAL -> RiskLevel.LOW
            IngredientCategory.UNKNOWN -> RiskLevel.MEDIUM
            else -> RiskLevel.MEDIUM
        }
    }
    
    private fun isHiddenSugar(ingredient: String): Boolean {
        return sugarSynonyms.any { ingredient.contains(it) }
    }
    
    private fun isTransFat(ingredient: String): Boolean {
        return transFatKeywords.any { ingredient.contains(it) }
    }
    
    private fun isArtificialColorant(ingredient: String): Boolean {
        return artificialColorants.any { ingredient.contains(it) }
    }
    
    private fun isPreservative(ingredient: String): Boolean {
        return preservatives.any { ingredient.contains(it) }
    }
    
    private fun isNaturalIngredient(ingredient: String): Boolean {
        val naturalKeywords = setOf(
            "agua", "sal", "azúcar", "harina", "aceite", "vinagre", "limón",
            "tomate", "cebolla", "ajo", "especias", "hierbas", "leche", "huevo"
        )
        return naturalKeywords.any { ingredient.contains(it) }
    }
    
    private fun isUltraProcessed(ingredient: String): Boolean {
        val ultraProcessedKeywords = setOf(
            "proteína aislada", "almidón modificado", "aceite refinado",
            "emulsificante", "estabilizante", "espesante", "gelificante"
        )
        return ultraProcessedKeywords.any { ingredient.contains(it) }
    }
    
    private fun isProcessed(ingredient: String): Boolean {
        val processedKeywords = setOf(
            "concentrado", "extracto", "polvo", "deshidratado", "refinado"
        )
        return processedKeywords.any { ingredient.contains(it) }
    }
    
    private fun isAdditive(ingredient: String): Boolean {
        return ingredient.startsWith("e") && ingredient.length == 4 && 
               ingredient.substring(1).all { it.isDigit() }
    }
    
    private fun findSynonyms(ingredient: String): List<String> {
        val lowerIngredient = ingredient.lowercase()
        return when {
            isHiddenSugar(lowerIngredient) -> sugarSynonyms.filter { it != lowerIngredient }
            else -> emptyList()
        }
    }
    
    private fun getIngredientDescription(ingredient: String, category: IngredientCategory): String? {
        return when (category) {
            IngredientCategory.HIDDEN_SUGAR -> "Azúcar añadido que puede estar oculto bajo otro nombre"
            IngredientCategory.TRANS_FAT -> "Grasa trans artificial, evitar su consumo"
            IngredientCategory.COLORANT -> "Colorante artificial que puede causar reacciones adversas"
            IngredientCategory.PRESERVATIVE -> "Conservante utilizado para prolongar la vida útil"
            IngredientCategory.ULTRA_PROCESSED -> "Ingrediente altamente procesado industrialmente"
            else -> null
        }
    }
}