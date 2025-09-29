package com.nutritionscanner.presentation.ui.marketplace

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.nutritionscanner.domain.model.MarketplaceProduct

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketplaceScreen(navController: NavHostController) {
    val demoProducts = getDemoMarketplaceProducts()
    
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Top app bar
        CenterAlignedTopAppBar(
            title = {
                Text(
                    "Marketplace Saludable",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        )
        
        // Info card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Star,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = Color(0xFF7B68EE)
                )
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Compra con Stellar XLM",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Productos locales orgánicos y saludables",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
        
        // Products grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(demoProducts) { product ->
                MarketplaceProductCard(product) {
                    // TODO: Handle product selection
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketplaceProductCard(
    product: MarketplaceProduct,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            // Product image placeholder
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        when (product.category) {
                            "Frutas" -> Icons.Default.LocalFlorist
                            "Verduras" -> Icons.Default.Eco
                            "Granos" -> Icons.Default.Grain
                            else -> Icons.Default.Store
                        },
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Product info
            Text(
                text = product.name,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            
            Text(
                text = product.seller,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Text(
                text = product.location,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Price and organic badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${product.price} XLM",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF7B68EE)
                )
                
                if (product.isOrganic) {
                    AssistChip(
                        onClick = { },
                        label = {
                            Text(
                                "Orgánico",
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Eco,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = Color(0xFFE8F5E8),
                            labelColor = Color(0xFF4CAF50),
                            leadingIconContentColor = Color(0xFF4CAF50)
                        )
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Buy button
            Button(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF7B68EE)
                )
            ) {
                Icon(
                    Icons.Default.ShoppingCart,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Comprar")
            }
        }
    }
}

private fun getDemoMarketplaceProducts(): List<MarketplaceProduct> {
    return listOf(
        MarketplaceProduct(
            id = "1",
            name = "Paltas Orgánicas",
            description = "Paltas frescas cultivadas sin pesticidas",
            price = 0.5,
            seller = "Fundo Los Andes",
            category = "Frutas",
            location = "Valparaíso, CL",
            isOrganic = true,
            rating = 4.8f,
            available = true
        ),
        MarketplaceProduct(
            id = "2",
            name = "Quinoa del Norte",
            description = "Quinoa premium cultivada en el altiplano chileno",
            price = 1.2,
            seller = "Granos Andinos",
            category = "Granos",
            location = "Arica, CL",
            isOrganic = true,
            rating = 4.9f,
            available = true
        ),
        MarketplaceProduct(
            id = "3",
            name = "Tomates Cherry",
            description = "Tomates hidropónicos locales",
            price = 0.3,
            seller = "Huertos del Maipo",
            category = "Verduras",
            location = "Santiago, CL",
            isOrganic = false,
            rating = 4.5f,
            available = true
        ),
        MarketplaceProduct(
            id = "4",
            name = "Miel de Ulmo",
            description = "Miel artesanal de bosque nativo chileno",
            price = 2.0,
            seller = "Colmenas del Sur",
            category = "Endulzantes",
            location = "Los Lagos, CL",
            isOrganic = true,
            rating = 5.0f,
            available = true
        ),
        MarketplaceProduct(
            id = "5",
            name = "Chía Orgánica",
            description = "Semillas de chía certificadas del desierto de Atacama",
            price = 0.8,
            seller = "Semillas del Desierto",
            category = "Semillas",
            location = "Atacama, CL",
            isOrganic = true,
            rating = 4.7f,
            available = true
        ),
        MarketplaceProduct(
            id = "6",
            name = "Espinacas Frescas",
            description = "Espinacas hidropónicas del valle central",
            price = 0.4,
            seller = "Verde Fresco",
            category = "Verduras",
            location = "Rancagua, CL",
            isOrganic = false,
            rating = 4.3f,
            available = true
        )
    )
}