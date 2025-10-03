package com.nutritionscanner.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginRequest(
    val email: String,
    val password: String
) : Parcelable

@Parcelize
data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val confirmPassword: String
) : Parcelable

@Parcelize
data class AuthResult(
    val success: Boolean,
    val user: User? = null,
    val errorMessage: String? = null
) : Parcelable

@Parcelize
data class ValidationResult(
    val isValid: Boolean,
    val errorMessage: String? = null
) : Parcelable

sealed class AuthState {
    object Initial : AuthState()
    object Loading : AuthState()
    data class Success(val user: User) : AuthState()
    data class Error(val message: String) : AuthState()
    object LoggedOut : AuthState()
}