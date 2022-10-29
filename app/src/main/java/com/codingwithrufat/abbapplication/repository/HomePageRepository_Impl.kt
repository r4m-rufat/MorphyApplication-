package com.codingwithrufat.abbapplication.repository

import com.codingwithrufat.abbapplication.network.RetrofitService
import com.codingwithrufat.abbapplication.network.model.MorphyResponse
import com.codingwithrufat.abbapplication.utils.NetworkResponse
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class HomePageRepository_Impl @Inject constructor(
    private val service: RetrofitService
): HomePageRepository {

    override suspend fun getAllData(queries: Map<String, String>) = flow {

            val response = service.getMorphyResponse(queries)

            if (response.isSuccessful)
                response.body()?.let {
                    emit(NetworkResponse.SUCCEED<MorphyResponse>(it))
                }
            else
                emit(NetworkResponse.ERROR<MorphyResponse>(response.message()))


    }.catch {
        emit(NetworkResponse.ERROR<MorphyResponse>("There is an exception"))
    }.flowOn(IO)

}