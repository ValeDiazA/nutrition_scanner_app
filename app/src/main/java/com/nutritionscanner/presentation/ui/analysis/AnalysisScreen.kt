package com.nutritionscanner.presentation.ui.analysis

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.nutritionscanner.domain.model.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalysisScreen(productId: String, navController: NavHostController) {
    // Demo data - in real app this would come from ViewModel
    val demoProduct = createDemoProduct()
    
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            // Top app bar
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Análisis Nutricional",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
        
        item {
            ProductInfoCard(demoProduct)
        }
        
        item {
            ScoreCard(demoProduct.score)
        }
        
        item {
            LatamLabelingCard(demoProduct.latamLabeling)
        }
        
        item {
            Text(
                text = "Ingredientes Analizados",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
        
        items(demoProduct.ingredients) { ingredient ->
            IngredientCard(ingredient)
        }
        
        item {
            RewardCard()
        }
    }
}

@Composable
fun ProductInfoCard(product: Product) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = product.name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            
            Text(
                text = product.brand,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Puntuación General",
                    style = MaterialTheme.typography.titleMedium
                )
                
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .background(
                                color = when (product.score.trafficLight) {
                                    TrafficLight.GREEN -> Color(0xFF4CAF50)
                                    TrafficLight.YELLOW -> Color(0xFFFF9800)
                                    TrafficLight.RED -> Color(0xFFF44336)
                                },
                                shape = CircleShape
                            )
                    )
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    Text(
                        text = "${product.score.overall.toInt()}/100",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun ScoreCard(score: NutritionalScore) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Desglose de Puntuación",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            ScoreItem("Ingredientes Naturales", score.breakdown.naturalIngredients)
            ScoreItem("Nivel de Procesamiento", score.breakdown.processedScore)
            ScoreItem("Aditivos", score.breakdown.additivesScore)
            ScoreItem("Azúcares", score.breakdown.sugarScore)
            ScoreItem("Grasas", score.breakdown.fatScore)
            ScoreItem("Sodio", score.breakdown.sodiumScore)
        }
    }
}

@Composable
fun ScoreItem(label: String, score: Float) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
        
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            LinearProgressIndicator(
                progress = score / 100f,
                modifier = Modifier.width(60.dp),
                color = when {
                    score >= 70 -> Color(0xFF4CAF50)
                    score >= 40 -> Color(0xFFFF9800)
                    else -> Color(0xFFF44336)
                }
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Text(
                text = "${score.toInt()}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun IngredientCard(ingredient: Ingredient) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = when (ingredient.riskLevel) {
                RiskLevel.LOW -> MaterialTheme.colorScheme.surfaceVariant
                RiskLevel.MEDIUM -> Color(0xFFFFF3E0)
                RiskLevel.HIGH -> Color(0xFFFFEBEE)
                RiskLevel.CRITICAL -> Color(0xFFFFCDD2)
            }
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = when (ingredient.category) {
                    IngredientCategory.NATURAL -> Icons.Default.Eco
                    IngredientCategory.HIDDEN_SUGAR -> Icons.Default.Warning
                    IngredientCategory.TRANS_FAT -> Icons.Default.Dangerous
                    IngredientCategory.COLORANT -> Icons.Default.Palette
                    else -> Icons.Default.Science
                },
                contentDescription = null,
                tint = when (ingredient.riskLevel) {
                    RiskLevel.LOW -> Color(0xFF4CAF50)
                    RiskLevel.MEDIUM -> Color(0xFFFF9800)
                    RiskLevel.HIGH -> Color(0xFFF44336)
                    RiskLevel.CRITICAL -> Color(0xFFD32F2F)
                }
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = ingredient.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                
                Text(
                    text = ingredient.category.name.lowercase().replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                ingredient.description?.let { description ->
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            @OptIn(ExperimentalMaterial3Api::class)
            FilterChip(
                selected = false,
                onClick = { },
                label = {
                    Text(
                        text = when (ingredient.riskLevel) {
                            RiskLevel.LOW -> "Bajo"
                            RiskLevel.MEDIUM -> "Medio"
                            RiskLevel.HIGH -> "Alto"
                            RiskLevel.CRITICAL -> "Crítico"
                        },
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            )
        }
    }
}

@Composable
fun LatamLabelingCard(latamLabeling: LatamLabeling?) {
    latamLabeling?.let { labeling ->
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "Etiquetado LATAM - ${labeling.country}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                labeling.warnings.forEach { warning ->
                    if (warning.isPresent) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            Icon(
                                Icons.Default.Warning,
                                contentDescription = null,
                                tint = Color(0xFFFF9800),
                                modifier = Modifier.size(20.dp)
                            )
                            
                            Spacer(modifier = Modifier.width(8.dp))
                            
                            Text(
                                text = getSealText(warning.type),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RewardCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.Star,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = Color(0xFF7B68EE)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "¡Recompensa Stellar!",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            
            Text(
                text = "Has ganado 0.1 XLM por escanear este producto",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = { /* TODO: Claim reward */ }
            ) {
                Text("Reclamar Recompensa")
            }
        }
    }
}

private fun getSealText(sealType: SealType): String {
    return when (sealType) {
        SealType.EXCESS_CALORIES -> "Exceso en Calorías"
        SealType.EXCESS_SODIUM -> "Exceso en Sodio"
        SealType.EXCESS_SUGAR -> "Exceso en Azúcares"
        SealType.EXCESS_SATURATED_FATS -> "Exceso en Grasas Saturadas"
        SealType.CONTAINS_SWEETENERS -> "Contiene Edulcorantes"
        SealType.CONTAINS_CAFFEINE -> "Contiene Cafeína"
    }
}

private fun createDemoProduct(): Product {
    return Product(
        id = "demo_product",
        name = "Galletas Chocolate",
        brand = "Marca Demo",
        ingredients = listOf(
            Ingredient(
                name = "Harina de trigo",
                originalText = "harina de trigo",
                category = IngredientCategory.PROCESSED,
                riskLevel = RiskLevel.LOW
            ),
            Ingredient(
                name = "Jarabe de maíz de alta fructosa",
                originalText = "jarabe de maíz de alta fructosa",
                category = IngredientCategory.HIDDEN_SUGAR,
                riskLevel = RiskLevel.HIGH,
                description = "Azúcar añadido que puede estar oculto bajo otro nombre"
            ),
            Ingredient(
                name = "Aceite parcialmente hidrogenado",
                originalText = "aceite parcialmente hidrogenado",
                category = IngredientCategory.TRANS_FAT,
                riskLevel = RiskLevel.CRITICAL,
                description = "Grasa trans artificial, evitar su consumo"
            ),
            Ingredient(
                name = "Tartrazina",
                originalText = "colorante amarillo 5",
                category = IngredientCategory.COLORANT,
                riskLevel = RiskLevel.HIGH,
                description = "Colorante artificial que puede causar reacciones adversas"
            )
        ),
        nutritionalInfo = NutritionalInfo(
            calories = 450.0,
            totalFat = 18.0,
            saturatedFat = 8.0,
            transFat = 2.0,
            sodium = 350.0,
            totalCarbs = 65.0,
            sugar = 25.0,
            addedSugar = 20.0,
            protein = 6.0,
            fiber = 2.0
        ),
        score = NutritionalScore(
            overall = 35f,
            trafficLight = TrafficLight.RED,
            breakdown = ScoreBreakdown(
                naturalIngredients = 25f,
                processedScore = 45f,
                additivesScore = 20f,
                sugarScore = 15f,
                fatScore = 10f,
                sodiumScore = 60f
            )
        ),
        latamLabeling = LatamLabeling(
            country = "CL",
            warnings = listOf(
                WarningSeal(SealType.EXCESS_CALORIES, true),
                WarningSeal(SealType.EXCESS_SUGAR, true),
                WarningSeal(SealType.EXCESS_SATURATED_FATS, true),
                WarningSeal(SealType.CONTAINS_SWEETENERS, false),
                WarningSeal(SealType.CONTAINS_CAFFEINE, false)
            )
        )
    )
}