package com.nutritionscanner.data.database

import androidx.room.*
import com.nutritionscanner.domain.model.*

@Database(
    entities = [ProductEntity::class, UserEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DatabaseConverters::class)
abstract class NutritionDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun userDao(): UserDao
}

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: String,
    val name: String,
    val brand: String,
    val barcode: String?,
    val ingredients: List<Ingredient>,
    val nutritionalInfo: NutritionalInfo?,
    val score: NutritionalScore,
    val latamLabeling: LatamLabeling?,
    val scanTimestamp: Long
)

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val name: String,
    val email: String,
    val level: Int,
    val totalPoints: Int,
    val stellarPublicKey: String?,
    val stellarBalance: Double,
    val scannedProducts: Int,
    val healthyChoices: Int,
    val totalDonations: Double,
    val achievements: List<Achievement>,
    val preferences: UserPreferences
)