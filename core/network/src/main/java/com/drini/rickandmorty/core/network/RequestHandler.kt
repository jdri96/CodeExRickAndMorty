package com.drini.rickandmorty.core.network

import retrofit2.HttpException
import retrofit2.Response

suspend fun <T : Any> handleRequest(
    execute: suspend () -> Response<T>
): Request<T> {
    return try {
        val response = execute()
        val body = response.body()

        if (response.isSuccessful && body != null) {
            Request.Success(body)
        } else {
            Request.Error(code = response.code(), message = response.message())
        }
    } catch (e: HttpException) {
        Request.Error(code = e.code(), message = e.message())
    } catch (e: Throwable) {
        Request.Exception(e)
    }
}
