package com.nutritionscanner.data.stellar

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nutritionscanner.domain.model.DonationProject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "stellar_preferences")

@Singleton
class StellarPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val gson = Gson()
    
    companion object {
        private val WALLET_KEY = stringPreferencesKey("stellar_wallet")
        private val TRANSACTION_HISTORY_KEY = stringPreferencesKey("transaction_history")
        private val DONATION_PROJECTS_KEY = stringPreferencesKey("donation_projects")
    }
    
    suspend fun saveWallet(wallet: StellarWallet) {
        context.dataStore.edit { preferences ->
            preferences[WALLET_KEY] = gson.toJson(wallet)
        }
    }
    
    suspend fun getWallet(): StellarWallet? {
        return context.dataStore.data.map { preferences ->
            preferences[WALLET_KEY]?.let { json ->
                gson.fromJson(json, StellarWallet::class.java)
            }
        }.first()
    }
    
    fun getWalletFlow(): Flow<StellarWallet?> {
        return context.dataStore.data.map { preferences ->
            preferences[WALLET_KEY]?.let { json ->
                gson.fromJson(json, StellarWallet::class.java)
            }
        }
    }
    
    suspend fun saveTransactionHistory(transactions: List<StellarTransactionRecord>) {
        context.dataStore.edit { preferences ->
            preferences[TRANSACTION_HISTORY_KEY] = gson.toJson(transactions)
        }
    }
    
    suspend fun getTransactionHistory(): List<StellarTransactionRecord> {
        return context.dataStore.data.map { preferences ->
            preferences[TRANSACTION_HISTORY_KEY]?.let { json ->
                val type = object : TypeToken<List<StellarTransactionRecord>>() {}.type
                gson.fromJson<List<StellarTransactionRecord>>(json, type)
            } ?: emptyList()
        }.first()
    }
    
    suspend fun saveDonationProjects(projects: List<DonationProject>) {
        context.dataStore.edit { preferences ->
            preferences[DONATION_PROJECTS_KEY] = gson.toJson(projects)
        }
    }
    
    suspend fun getDonationProjects(): List<DonationProject> {
        return context.dataStore.data.map { preferences ->
            preferences[DONATION_PROJECTS_KEY]?.let { json ->
                val type = object : TypeToken<List<DonationProject>>() {}.type
                gson.fromJson<List<DonationProject>>(json, type)
            } ?: getDefaultDonationProjects()
        }.first()
    }
    
    private fun getDefaultDonationProjects(): List<DonationProject> {
        return listOf(
                DonationProject(
                    id = "nutrition_chile_1",
                    name = "Alimentación Escolar Rural Chile",
                    description = "Programa de alimentación saludable para escuelas rurales en el sur de Chile",
                    targetAmount = 15000.0,
                    currentAmount = 7500.0,
                    country = "CL"
                ),
                DonationProject(
                    id = "nutrition_chile_2",
                    name = "Huertas Comunitarias Urbanas",
                    description = "Creación de huertos urbanos en poblaciones vulnerables de Santiago y Valparaíso",
                    targetAmount = 12000.0,
                    currentAmount = 5800.0,
                    country = "CL"
                ),
                DonationProject(
                    id = "nutrition_chile_3",
                    name = "Educación Nutricional Mapuche",
                    description = "Talleres de educación alimentaria en comunidades mapuches de La Araucanía",
                    targetAmount = 8000.0,
                    currentAmount = 3200.0,
                    country = "CL"
                )
        )
    }
}