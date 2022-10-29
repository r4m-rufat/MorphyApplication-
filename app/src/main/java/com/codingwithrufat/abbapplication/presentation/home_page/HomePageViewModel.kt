package com.codingwithrufat.abbapplication.presentation.home_page

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codingwithrufat.abbapplication.network.model.MorphyResponse
import com.codingwithrufat.abbapplication.repository.HomePageRepository
import com.codingwithrufat.abbapplication.utils.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomePageViewModel @Inject constructor(
    private val repository: HomePageRepository
) : ViewModel() {

    private val morphyResponse =
        MutableLiveData<NetworkResponse<MorphyResponse>>(NetworkResponse.LOADING())

    val data: LiveData<NetworkResponse<MorphyResponse>>
        get() = morphyResponse

    fun getAllData(queries: Map<String, String>) = viewModelScope.launch {

        repository.getAllData(queries).collect { response ->

            morphyResponse.value = response

        }

    }

}