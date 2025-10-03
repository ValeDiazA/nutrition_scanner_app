package com.nutritionscanner.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM products ORDER BY scanTimestamp DESC")
    fun getAllProducts(): Flow<List<ProductEntity>>
    
    @Query("SELECT * FROM products WHERE id = :id")
    suspend fun getProductById(id: String): ProductEntity?
    
    @Query("SELECT * FROM products ORDER BY scanTimestamp DESC LIMIT :limit")
    fun getRecentProducts(limit: Int): Flow<List<ProductEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity)
    
    @Delete
    suspend fun deleteProduct(product: ProductEntity)
    
    @Query("DELETE FROM products")
    suspend fun deleteAllProducts()
    
    @Query("SELECT COUNT(*) FROM products WHERE JSON_EXTRACT(score, '$.trafficLight') = 'GREEN'")
    suspend fun getHealthyProductsCount(): Int
}

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUserById(id: String): UserEntity?
    
    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun getUserByEmail(email: String): UserEntity?
    
    @Query("SELECT * FROM users WHERE email = :email AND passwordHash = :passwordHash")
    suspend fun loginUser(email: String, passwordHash: String): UserEntity?
    
    @Query("SELECT * FROM users WHERE isLoggedIn = 1 LIMIT 1")
    suspend fun getLoggedInUser(): UserEntity?
    
    @Query("UPDATE users SET isLoggedIn = 1, lastLoginAt = :loginTime WHERE id = :userId")
    suspend fun markUserAsLoggedIn(userId: String, loginTime: Long)
    
    @Query("UPDATE users SET isLoggedIn = 0 WHERE id = :userId")
    suspend fun markUserAsLoggedOut(userId: String)
    
    @Query("UPDATE users SET isLoggedIn = 0")
    suspend fun logoutAllUsers()
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)
    
    @Update
    suspend fun updateUser(user: UserEntity)
    
    @Query("UPDATE users SET totalPoints = totalPoints + :points WHERE id = :userId")
    suspend fun addPoints(userId: String, points: Int)
    
    @Query("UPDATE users SET scannedProducts = scannedProducts + 1 WHERE id = :userId")
    suspend fun incrementScannedProducts(userId: String)
    
    @Query("UPDATE users SET healthyChoices = healthyChoices + 1 WHERE id = :userId")
    suspend fun incrementHealthyChoices(userId: String)
}