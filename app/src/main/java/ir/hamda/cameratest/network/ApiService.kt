package ir.hamda.cameratest.network

import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

    //upload
    @Multipart
    @POST("/api/client/pooldoc")
    suspend fun upload(
            @Header("Authorization") header: String,
            @Part("type") type: RequestBody,
            @Part file: MultipartBody.Part,
            @Part("customer_id") customerId: RequestBody,
            @Part("priority") priority: RequestBody,

    ): JsonObject


}