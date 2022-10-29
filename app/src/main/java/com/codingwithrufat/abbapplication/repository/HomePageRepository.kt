package com.codingwithrufat.abbapplication.repository

import com.codingwithrufat.abbapplication.network.model.MorphyResponse
import com.codingwithrufat.abbapplication.utils.NetworkResponse
import kotlinx.coroutines.flow.Flow

interface HomePageRepository {
    suspend fun getAllData(queries: Map<String, String>): Flow<NetworkResponse<MorphyResponse>>
}