package com.codingwithrufat.abbapplication.utils

sealed class NetworkResponse<T>(var data: T? = null, val msg: String? = null) {
    class SUCCEED<T>(data: T): NetworkResponse<T>(data)
    class LOADING<T>(): NetworkResponse<T>()
    class ERROR<T>(error_msg: String): NetworkResponse<T>(null, error_msg)
}