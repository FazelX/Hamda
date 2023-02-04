package ir.hamda.cameratest.network.util

import retrofit2.HttpException

sealed class ApiState{
    object Loading:ApiState()
    class Failure(val t:Throwable) : ApiState()
    class Success<out T>(val data:T) : ApiState()
    object Empty : ApiState()

}