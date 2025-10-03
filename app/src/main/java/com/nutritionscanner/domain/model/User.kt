package com.nutritionscanner.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: String,
    val name: String,
    val email: String,
    val isLoggedIn: Boolean = false,
    val createdAt: Long,
    val lastLoginAt: Long?,
    val level: Int = 1,
    val totalPoints: Int = 0,
    val stellarPublicKey: String? = null,
    val stellarBalance: Double = 0.0,
    val scannedProducts: Int = 0,
    val healthyChoices: Int = 0,
    val totalDonations: Double = 0.0,
    val achievements: List<Achievement> = emptyList(),
    val preferences: UserPreferences = UserPreferences()
) : Parcelable

@Parcelize
data class UserPreferences(
    val language: String = "es",
    val country: String = "CL",
    val dietaryRestrictions: List<String> = emptyList(),
    val notificationsEnabled: Boolean = true,
    val offlineMode: Boolean = false
) : Parcelable

@Parcelize
data class Achievement(
    val id: String,
    val name: String,
    val description: String,
    val iconRes: String,
    val unlockedAt: Long?,
    val progress: Int = 0,
    val target: Int = 1,
    val stellarReward: Double = 0.0
) : Parcelable