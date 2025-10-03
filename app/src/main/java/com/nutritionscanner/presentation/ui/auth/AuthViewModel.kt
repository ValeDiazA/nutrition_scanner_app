package com.nutritionscanner.presentation.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nutritionscanner.domain.model.AuthState
import com.nutritionscanner.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val checkAuthStatusUseCase: CheckAuthStatusUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    private val validateNameUseCase: ValidateNameUseCase,
    private val validateConfirmPasswordUseCase: ValidateConfirmPasswordUseCase,
    private val validateLoginPasswordUseCase: ValidateLoginPasswordUseCase
) : ViewModel() {
    
    private val _authState = MutableStateFlow<AuthState>(AuthState.Initial)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()
    
    init {
        checkCurrentUser()
    }
    
    fun login(email: String, password: String, rememberMe: Boolean = true) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            
            loginUseCase(email, password, rememberMe).collect { result ->
                _authState.value = if (result.success && result.user != null) {
                    AuthState.Success(result.user)
                } else {
                    AuthState.Error(result.errorMessage ?: "Error desconocido durante el login")
                }
            }
        }
    }
    
    fun register(name: String, email: String, password: String, confirmPassword: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            
            registerUseCase(name, email, password, confirmPassword).collect { result ->
                _authState.value = if (result.success && result.user != null) {
                    AuthState.Success(result.user)
                } else {
                    AuthState.Error(result.errorMessage ?: "Error desconocido durante el registro")
                }
            }
        }
    }
    
    fun logout() {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            
            logoutUseCase().collect { result ->
                _authState.value = if (result.success) {
                    AuthState.LoggedOut
                } else {
                    AuthState.Error(result.errorMessage ?: "Error durante el logout")
                }
            }
        }
    }
    
    fun checkCurrentUser() {
        viewModelScope.launch {
            try {
                val isLoggedIn = checkAuthStatusUseCase()
                if (!isLoggedIn) {
                    _authState.value = AuthState.LoggedOut
                    return@launch
                }
                
                getCurrentUserUseCase().collect { result ->
                    _authState.value = if (result.success && result.user != null) {
                        AuthState.Success(result.user)
                    } else {
                        AuthState.LoggedOut
                    }
                }
            } catch (e: Exception) {
                _authState.value = AuthState.LoggedOut
            }
        }
    }
    
    fun clearError() {
        if (_authState.value is AuthState.Error) {
            _authState.value = AuthState.Initial
        }
    }
    
    // Métodos de validación
    fun validateEmail(email: String): String? {
        return validateEmailUseCase(email)
    }
    
    fun validatePassword(password: String): String? {
        return validatePasswordUseCase(password)
    }
    
    fun validateName(name: String): String? {
        return validateNameUseCase(name)
    }
    
    fun validateConfirmPassword(password: String, confirmPassword: String): String? {
        return validateConfirmPasswordUseCase(password, confirmPassword)
    }
    
    fun validateLoginPassword(password: String): String? {
        return validateLoginPasswordUseCase(password)
    }
}

// ViewModel para el estado global de autenticación de la aplicación
@HiltViewModel
class AppAuthViewModel @Inject constructor(
    private val checkAuthStatusUseCase: CheckAuthStatusUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {
    
    private val _isAuthenticated = MutableStateFlow<Boolean?>(null)
    val isAuthenticated: StateFlow<Boolean?> = _isAuthenticated.asStateFlow()
    
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    init {
        checkAuthenticationStatus()
    }
    
    fun checkAuthenticationStatus() {
        viewModelScope.launch {
            _isLoading.value = true
            
            try {
                val isLoggedIn = checkAuthStatusUseCase()
                
                if (isLoggedIn) {
                    getCurrentUserUseCase().collect { result ->
                        _isAuthenticated.value = result.success && result.user != null
                        _isLoading.value = false
                    }
                } else {
                    _isAuthenticated.value = false
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                _isAuthenticated.value = false
                _isLoading.value = false
            }
        }
    }
    
    fun refreshAuthStatus() {
        checkAuthenticationStatus()
    }
}