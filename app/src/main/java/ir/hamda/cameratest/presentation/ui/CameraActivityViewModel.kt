package ir.hamda.cameratest.presentation.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.hamda.cameratest.network.util.Upload

import javax.inject.Inject

@HiltViewModel
class CameraActivityViewModel
@Inject
constructor(
	private val upload: Upload
) : ViewModel() {

    val _upload get() = upload

}