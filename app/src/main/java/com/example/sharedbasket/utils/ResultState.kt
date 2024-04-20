package com.example.sharedbasket.utils

sealed class ResultState<out R>{
    data class Success<out R>(val data:R):ResultState<R>()
    data class Failure(val msg:Throwable):ResultState<Nothing>()
    object Loading : ResultState<Nothing>()
}

sealed class ResultExist{
    object Exist : ResultExist()
    object notExist : ResultExist()
    object Error : ResultExist()
}