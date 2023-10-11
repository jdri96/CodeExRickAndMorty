package com.drini.rickandmorty.core.network

sealed interface Request<T : Any> {
    class Success<T : Any>(val data: T) : Request<T>
    class Error<T : Any>(val code: Int, val message: String?) : Request<T>
    class Exception<T : Any>(val e: Throwable) : Request<T>
}
