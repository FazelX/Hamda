package ir.hamda.cameratest.network

import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import javax.inject.Inject

data class ApiRepository
@Inject
constructor(
        private val apiServiceImpl: ApiServiceImpl
) {

    fun upload(body: MultipartBody.Part): Flow<JsonObject> = flow {
        emit(apiServiceImpl.upload(body))
    }.flowOn(Dispatchers.IO)

}
