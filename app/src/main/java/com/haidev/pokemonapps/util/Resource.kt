package com.haidev.pokemonapps.util

sealed class Resource<T>(
    val data: T? = null,
    val messageError: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class Error<T>(messageError: String) : Resource<T>(null, messageError)

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$messageError]"
            is Loading<T> -> "Loading"
        }
    }
}