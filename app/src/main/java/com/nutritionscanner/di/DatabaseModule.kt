package com.nutritionscanner.di

import android.content.Context
import androidx.room.Room
import com.nutritionscanner.data.database.NutritionDatabase
import com.nutritionscanner.data.database.ProductDao
import com.nutritionscanner.data.database.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideNutritionDatabase(@ApplicationContext context: Context): NutritionDatabase {
        return Room.databaseBuilder(
            context,
            NutritionDatabase::class.java,
            "nutrition_database"
        ).build()
    }
    
    @Provides
    fun provideProductDao(database: NutritionDatabase): ProductDao {
        return database.productDao()
    }
    
    @Provides
    fun provideUserDao(database: NutritionDatabase): UserDao {
        return database.userDao()
    }
}