package com.bangkit.huggingpet.di

import android.content.Context
import com.bangkit.huggingpet.api.ApiConfig
import com.bangkit.huggingpet.database.PetDatabase
import com.bangkit.huggingpet.repository.MainRepository

object Injection {
    fun provideRepository(context: Context): MainRepository {
        val database = PetDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return MainRepository(database, apiService)
    }
}