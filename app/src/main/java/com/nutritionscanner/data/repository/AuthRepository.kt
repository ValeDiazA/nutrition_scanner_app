package com.nutritionscanner.data.repository

import com.nutritionscanner.data.auth.AuthPreferences
import com.nutritionscanner.data.auth.PasswordUtils
import com.nutritionscanner.data.database.UserDao
import com.nutritionscanner.data.database.UserEntity
import com.nutritionscanner.domain.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val userDao: UserDao,
    private val authPreferences: AuthPreferences
) {
    
    suspend fun login(email: String, password: String, rememberMe: Boolean): Flow<AuthResult> = flow {
        try {
            emit(AuthResult(success = false, errorMessage = "Iniciando sesión..."))
            
            // Buscar usuario por email
            val userEntity = userDao.getUserByEmail(email)
                ?: return@flow emit(AuthResult(success = false, errorMessage = "Email no registrado"))
            
            // Verificar contraseña
            if (!PasswordUtils.verifyPassword(password, userEntity.passwordHash)) {
                return@flow emit(AuthResult(success = false, errorMessage = "Contraseña incorrecta"))
            }
            
            // Actualizar estado de login en la base de datos
            val currentTime = System.currentTimeMillis()
            userDao.markUserAsLoggedIn(userEntity.id, currentTime)
            
            // Guardar sesión en preferencias
            if (rememberMe) {
                authPreferences.saveUserSession(userEntity.id, userEntity.email, userEntity.name, true)
            }
            
            // Convertir entidad a modelo de dominio
            val user = mapEntityToDomain(userEntity.copy(
                isLoggedIn = true,
                lastLoginAt = currentTime
            ))
            
            emit(AuthResult(success = true, user = user))
            
        } catch (e: Exception) {
            emit(AuthResult(success = false, errorMessage = "Error durante el login: ${e.message}"))
        }
    }
    
    suspend fun register(
        name: String, 
        email: String, 
        password: String, 
        confirmPassword: String
    ): Flow<AuthResult> = flow {
        try {
            emit(AuthResult(success = false, errorMessage = "Creando cuenta..."))
            
            // Verificar si el email ya existe
            val existingUser = userDao.getUserByEmail(email)
            if (existingUser != null) {
                return@flow emit(AuthResult(success = false, errorMessage = "Este email ya está registrado"))
            }
            
            // Validar que las contraseñas coincidan
            if (password != confirmPassword) {
                return@flow emit(AuthResult(success = false, errorMessage = "Las contraseñas no coinciden"))
            }
            
            // Crear hash de la contraseña
            val passwordHash = PasswordUtils.hashPassword(password)
            
            // Crear usuario
            val currentTime = System.currentTimeMillis()
            val userId = UUID.randomUUID().toString()
            
            val userEntity = UserEntity(
                id = userId,
                name = name,
                email = email,
                passwordHash = passwordHash,
                isLoggedIn = true,
                createdAt = currentTime,
                lastLoginAt = currentTime,
                level = 1,
                totalPoints = 0,
                stellarPublicKey = null,
                stellarBalance = 0.0,
                scannedProducts = 0,
                healthyChoices = 0,
                totalDonations = 0.0,
                achievements = emptyList(),
                preferences = UserPreferences()
            )
            
            // Guardar en base de datos
            userDao.insertUser(userEntity)
            
            // Guardar sesión en preferencias
            authPreferences.saveUserSession(userId, email, name, true)
            
            // Convertir entidad a modelo de dominio
            val user = mapEntityToDomain(userEntity)
            
            emit(AuthResult(success = true, user = user))
            
        } catch (e: Exception) {
            emit(AuthResult(success = false, errorMessage = "Error durante el registro: ${e.message}"))
        }
    }
    
    suspend fun logout(): Flow<AuthResult> = flow {
        try {
            val currentUserId = authPreferences.getCurrentUserId()
            
            if (currentUserId != null) {
                // Marcar usuario como desconectado en la base de datos
                userDao.markUserAsLoggedOut(currentUserId)
            } else {
                // Si no hay usuario en preferencias, desconectar todos
                userDao.logoutAllUsers()
            }
            
            // Limpiar preferencias
            authPreferences.clearUserSession()
            
            emit(AuthResult(success = true, user = null))
            
        } catch (e: Exception) {
            emit(AuthResult(success = false, errorMessage = "Error durante el logout: ${e.message}"))
        }
    }
    
    suspend fun getCurrentUser(): Flow<AuthResult> = flow {
        try {
            val userId = authPreferences.getCurrentUserId()
            
            if (userId == null || !authPreferences.isLoggedIn()) {
                emit(AuthResult(success = false, errorMessage = "No hay sesión activa"))
                return@flow
            }
            
            val userEntity = userDao.getUserById(userId)
            if (userEntity == null) {
                // Usuario no existe en la base de datos, limpiar preferencias
                authPreferences.clearUserSession()
                emit(AuthResult(success = false, errorMessage = "Usuario no encontrado"))
                return@flow
            }
            
            if (!userEntity.isLoggedIn) {
                // Usuario marcado como desconectado, limpiar preferencias
                authPreferences.clearUserSession()
                emit(AuthResult(success = false, errorMessage = "Sesión expirada"))
                return@flow
            }
            
            val user = mapEntityToDomain(userEntity)
            emit(AuthResult(success = true, user = user))
            
        } catch (e: Exception) {
            emit(AuthResult(success = false, errorMessage = "Error obteniendo usuario actual: ${e.message}"))
        }
    }
    
    suspend fun isLoggedIn(): Boolean {
        return try {
            authPreferences.isLoggedIn() && 
            authPreferences.getCurrentUserId() != null &&
            userDao.getLoggedInUser() != null
        } catch (e: Exception) {
            false
        }
    }
    
    private fun mapEntityToDomain(entity: UserEntity): User {
        return User(
            id = entity.id,
            name = entity.name,
            email = entity.email,
            isLoggedIn = entity.isLoggedIn,
            createdAt = entity.createdAt,
            lastLoginAt = entity.lastLoginAt,
            level = entity.level,
            totalPoints = entity.totalPoints,
            stellarPublicKey = entity.stellarPublicKey,
            stellarBalance = entity.stellarBalance,
            scannedProducts = entity.scannedProducts,
            healthyChoices = entity.healthyChoices,
            totalDonations = entity.totalDonations,
            achievements = entity.achievements,
            preferences = entity.preferences
        )
    }
}