package com.example.planmyvacation.callbacks

interface ApiCallbacks<T> {
    fun returnResult(result: T?)
    fun returnError(message: String)
}