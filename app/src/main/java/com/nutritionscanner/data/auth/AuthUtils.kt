package com.nutritionscanner.data.auth

import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec
import android.util.Base64

object PasswordUtils {
    
    /**
     * Genera un hash seguro de la contraseña usando SHA-256 con salt
     */
    fun hashPassword(password: String, salt: String? = null): String {
        val actualSalt = salt ?: generateSalt()
        val passwordWithSalt = password + actualSalt
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(passwordWithSalt.toByteArray())
        return Base64.encodeToString(hashBytes, Base64.DEFAULT) + ":" + actualSalt
    }
    
    /**
     * Verifica si una contraseña coincide con el hash almacenado
     */
    fun verifyPassword(password: String, storedHash: String): Boolean {
        return try {
            val parts = storedHash.split(":")
            if (parts.size != 2) return false
            
            val hash = parts[0]
            val salt = parts[1]
            val newHash = hashPassword(password, salt)
            newHash == storedHash
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * Genera un salt aleatorio
     */
    private fun generateSalt(): String {
        val random = SecureRandom()
        val salt = ByteArray(16)
        random.nextBytes(salt)
        return Base64.encodeToString(salt, Base64.DEFAULT)
    }
    
    /**
     * Valida la fortaleza de la contraseña
     */
    fun validatePassword(password: String): Pair<Boolean, String?> {
        return when {
            password.length < 6 -> Pair(false, "La contraseña debe tener al menos 6 caracteres")
            !password.any { it.isUpperCase() } -> Pair(false, "La contraseña debe tener al menos una mayúscula")
            !password.any { it.isLowerCase() } -> Pair(false, "La contraseña debe tener al menos una minúscula")
            !password.any { it.isDigit() } -> Pair(false, "La contraseña debe tener al menos un número")
            else -> Pair(true, null)
        }
    }
}

object EmailValidator {
    
    /**
     * Valida si el email tiene un formato válido
     */
    fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }
    
    /**
     * Valida el email y retorna un resultado con mensaje de error si aplica
     */
    fun validateEmail(email: String): Pair<Boolean, String?> {
        return when {
            email.isEmpty() -> Pair(false, "El email es requerido")
            !isValidEmail(email) -> Pair(false, "El formato del email no es válido")
            else -> Pair(true, null)
        }
    }
}