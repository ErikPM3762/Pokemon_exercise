package com.example.data.utils


import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response


@ExperimentalCoroutinesApi
fun<T> Flow<NetResult<T>>.loading():Flow<NetResult<T>> =
    this.onStart { emit(NetResult.StarLoading) }
        .onCompletion { emit(NetResult.StopLoading) }

fun <T>Throwable.toNetworkResult(): Response<T> =
    Response.error(500, getGenericError().toResponseBody(null))

fun <T, R> Response<T>.parse(parseMethod : (T) -> R): NetResult<R> {
    val responseData = body()
    return if(isSuccessful && responseData != null)
        try {
            NetResult.Success(parseMethod(responseData))
        } catch (e : Exception){
            NetResult.Error(getGenericError())
        }else {
        NetResult.Error(getGenericError())
    }
}

fun Response<ResponseBody>.evaluate(): NetResult<Boolean> =
    if (isSuccessful)
        NetResult.Success(true)
    else
        NetResult.Error(getGenericError())


fun getGenericError() = "Ups... \n" +
        "No se ha podido establecer conexión con el servidor, comprueba el acceso a internet e inténtalo nuevamente."
