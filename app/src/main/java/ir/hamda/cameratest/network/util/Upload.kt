package ir.hamda.cameratest.network.util

import ir.hamda.cameratest.network.ApiRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class Upload @Inject constructor(private val apiRepository: ApiRepository) {

    private val uploadFlow: MutableStateFlow<ApiState> = MutableStateFlow(ApiState.Empty)

    val _uploadFlow: StateFlow<ApiState> = uploadFlow

    fun uploadFile(path: String) = CoroutineScope(Dispatchers.IO).launch {

        uploadFlow.value = ApiState.Loading

        if (!path.isNullOrEmpty() && path != "-") {

            // Map is used to multipart the file using okhttp3.RequestBody
            val file = File(path)

            // Parsing any Media type file
            val requestBody: RequestBody =
                file.asRequestBody("multipart/form-data".toMediaTypeOrNull())

            val fileToUpload: MultipartBody.Part =
                MultipartBody.Part.createFormData("attachments[image][]", file.name, requestBody)


            apiRepository.upload(body = fileToUpload)
                .catch { e ->
                    uploadFlow.value = ApiState.Failure(e)
                }.collect { data ->
                    uploadFlow.value = ApiState.Success(data)
                }


        } else {
            uploadFlow.value = ApiState.Failure(Throwable("File path is corrupted"))
        }

    }

}