package com.nutritionscanner.domain.usecase

import com.nutritionscanner.data.auth.EmailValidator
import com.nutritionscanner.data.auth.PasswordUtils
import com.nutritionscanner.data.repository.AuthRepository
import com.nutritionscanner.domain.model.AuthResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String, rememberMe: Boolean): Flow<AuthResult> {
        // Validaciones básicas
        val emailValidation = EmailValidator.validateEmail(email)
        if (!emailValidation.first) {
            return kotlinx.coroutines.flow.flow {
                emit(AuthResult(success = false, errorMessage = emailValidation.second))
            }
        }
        
        if (password.isEmpty()) {
            return kotlinx.coroutines.flow.flow {
                emit(AuthResult(success = false, errorMessage = "La contraseña es requerida"))
            }
        }
        
        return authRepository.login(email.trim().lowercase(), password, rememberMe)
    }
}

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        name: String, 
        email: String, 
        password: String, 
        confirmPassword: String
    ): Flow<AuthResult> {
        // Validaciones
        if (name.trim().isEmpty()) {
            return kotlinx.coroutines.flow.flow {
                emit(AuthResult(success = false, errorMessage = "El nombre es requerido"))
            }
        }
        
        if (name.trim().length < 2) {
            return kotlinx.coroutines.flow.flow {
                emit(AuthResult(success = false, errorMessage = "El nombre debe tener al menos 2 caracteres"))
            }
        }
        
        val emailValidation = EmailValidator.validateEmail(email)
        if (!emailValidation.first) {
            return kotlinx.coroutines.flow.flow {
                emit(AuthResult(success = false, errorMessage = emailValidation.second))
            }
        }
        
        val passwordValidation = PasswordUtils.validatePassword(password)
        if (!passwordValidation.first) {
            return kotlinx.coroutines.flow.flow {
                emit(AuthResult(success = false, errorMessage = passwordValidation.second))
            }
        }
        
        if (password != confirmPassword) {
            return kotlinx.coroutines.flow.flow {
                emit(AuthResult(success = false, errorMessage = "Las contraseñas no coinciden"))
            }
        }
        
        return authRepository.register(
            name.trim(), 
            email.trim().lowercase(), 
            password, 
            confirmPassword
        )
    }
}

class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Flow<AuthResult> {
        return authRepository.logout()
    }
}

class GetCurrentUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Flow<AuthResult> {
        return authRepository.getCurrentUser()
    }
}

class CheckAuthStatusUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Boolean {
        return authRepository.isLoggedIn()
    }
}

class ValidateEmailUseCase @Inject constructor() {
    operator fun invoke(email: String): String? {
        val validation = EmailValidator.validateEmail(email)
        return if (validation.first) null else validation.second
    }
}

class ValidatePasswordUseCase @Inject constructor() {
    operator fun invoke(password: String): String? {
        val validation = PasswordUtils.validatePassword(password)
        return if (validation.first) null else validation.second
    }
}

class ValidateNameUseCase @Inject constructor() {
    operator fun invoke(name: String): String? {
        return when {
            name.trim().isEmpty() -> "El nombre es requerido"
            name.trim().length < 2 -> "El nombre debe tener al menos 2 caracteres"
            name.trim().length > 50 -> "El nombre no puede tener más de 50 caracteres"
            !name.trim().matches(Regex("^[a-zA-ZÀ-ÿ\\s]+$")) -> "El nombre solo puede contener letras y espacios"
            else -> null
        }
    }
}

class ValidateConfirmPasswordUseCase @Inject constructor() {
    operator fun invoke(password: String, confirmPassword: String): String? {
        return when {
            confirmPassword.isEmpty() -> "Debes confirmar la contraseña"
            password != confirmPassword -> "Las contraseñas no coinciden"
            else -> null
        }
    }
}

class ValidateLoginPasswordUseCase @Inject constructor() {
    operator fun invoke(password: String): String? {
        return if (password.isEmpty()) "La contraseña es requerida" else null
    }
}