package com.codingwithrufat.abbapplication.network

import com.codingwithrufat.abbapplication.network.model.MorphyResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface RetrofitService {

    @GET("character/?")
    suspend fun getMorphyResponse(@QueryMap map: Map<String, String>): Response<MorphyResponse>

}