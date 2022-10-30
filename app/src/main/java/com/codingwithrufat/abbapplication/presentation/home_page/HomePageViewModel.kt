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

    private val queryMap = MutableLiveData<HashMap<String, String>>(HashMap())
    private val page = MutableLiveData(1)

    fun getAllData() = viewModelScope.launch {

        queryMap.value?.let {
            repository.getAllData(page.value!!, it).collect { response ->

                morphyResponse.value = response

            }
        }

    }

    fun updateResponseStateToLoading() {
        morphyResponse.value = NetworkResponse.LOADING()
    }

    fun incrementPageNumber() {
        page.value = page.value!! + 1
    }

    fun resetPaginationElements() {
        page.value = 1
    }

    fun updateQueryMap(newQueryMap: HashMap<String, String>) {
        queryMap.value = newQueryMap
    }

    fun updateQueryMapItem(key: String, value: String) {
        queryMap.value?.set(key, value)
    }

}