package com.example.myapplication.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

fun <T> safeApiCall(call: suspend() -> Response<T>): Flow<Resource<T>> = flow {
    emit(Resource.Loading())

    var remoteData: Response<T>? = null
    try {
        remoteData = call()
        val data = remoteData.body()
        emit(Resource.Success(data))
    } catch (e: HttpException) {
        emit(
            Resource.Error(
                message = Constants.HttpExceptionError,
                data = null
            )
        )
    } catch (e: IOException) {
        emit(
            Resource.Error(
                message = Constants.IOExceptionError,
                data = null
            )
        )
    }


}
