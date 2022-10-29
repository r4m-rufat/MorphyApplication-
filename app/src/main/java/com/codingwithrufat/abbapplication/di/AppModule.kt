package com.codingwithrufat.abbapplication.di

import com.codingwithrufat.abbapplication.network.RetrofitService
import com.codingwithrufat.abbapplication.repository.HomePageRepository
import com.codingwithrufat.abbapplication.repository.HomePageRepository_Impl
import com.codingwithrufat.abbapplication.utils.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideService() = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()
        .create(RetrofitService::class.java)

    @Provides
    fun provideHomeRepo(service: RetrofitService): HomePageRepository = HomePageRepository_Impl(service)

}