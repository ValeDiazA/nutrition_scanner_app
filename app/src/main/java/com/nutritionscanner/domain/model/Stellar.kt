package com.nutritionscanner.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StellarTransaction(
    val id: String,
    val type: TransactionType,
    val amount: Double,
    val description: String,
    val timestamp: Long,
    val txHash: String? = null,
    val status: TransactionStatus = TransactionStatus.PENDING
) : Parcelable

enum class TransactionType {
    REWARD_SCAN,
    REWARD_HEALTHY_CHOICE,
    DONATION,
    MARKETPLACE_PURCHASE,
    ACHIEVEMENT_BONUS
}

enum class TransactionStatus {
    PENDING,
    CONFIRMED,
    FAILED
}

@Parcelize
data class MarketplaceProduct(
    val id: String,
    val name: String,
    val description: String,
    val price: Double, // In XLM
    val seller: String,
    val imageUrl: String? = null,
    val category: String,
    val location: String,
    val isOrganic: Boolean = false,
    val rating: Float = 0f,
    val available: Boolean = true
) : Parcelable

@Parcelize
data class DonationProject(
    val id: String,
    val name: String,
    val description: String,
    val targetAmount: Double,
    val currentAmount: Double,
    val country: String,
    val imageUrl: String? = null,
    val isActive: Boolean = true
) : Parcelable