package ir.hamda.cameratest.network

import com.google.gson.JsonObject
import ir.hamda.cameratest.base.Config
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class ApiServiceImpl
@Inject
constructor(
        private val apiService: ApiService
) {


    suspend fun upload(body: MultipartBody.Part): JsonObject =
            apiService.upload(
                    Config.token,
                    RequestBody.create("text/plain".toMediaTypeOrNull(), Config.type),
                    body,
                    RequestBody.create("text/plain".toMediaTypeOrNull(), Config.customer_id.toString()),
                    RequestBody.create("text/plain".toMediaTypeOrNull(), Config.priority.toString())
            )

}