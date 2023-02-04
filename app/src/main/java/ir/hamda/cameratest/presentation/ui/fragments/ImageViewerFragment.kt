

package ir.hamda.cameratest.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import id.zelory.compressor.Compressor
import ir.hamda.cameratest.databinding.FragmentImageViewerBinding
import ir.hamda.cameratest.domain.model.UploadSuccessResponse
import ir.hamda.cameratest.network.util.ApiState
import ir.hamda.cameratest.presentation.ui.CameraActivity
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File


class ImageViewerFragment : Fragment() {


    /** Android ViewBinding */
    private var _fragmentImageViewerBinding: FragmentImageViewerBinding? = null

    private val fragmentImageViewerBinding get() = _fragmentImageViewerBinding!!

    /** AndroidX navigation arguments */
    private val args: ImageViewerFragmentArgs by navArgs()

    /** Captured image path */
    lateinit var capturedImagePath: String

    /** Local flag parameter to handle collector state */
    private var isUploadCollectorSet: Boolean = false



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _fragmentImageViewerBinding = FragmentImageViewerBinding.inflate(inflater, container, false)
        return fragmentImageViewerBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        capturedImagePath = args.filePath

        Glide.with(view).load(capturedImagePath).into(fragmentImageViewerBinding.capturedImage)

        fragmentImageViewerBinding.uploadImageFab.setOnClickListener {
            uploadCapturedImage()
        }

        fragmentImageViewerBinding.uploadImageCoverLyt.setOnClickListener {

        }

    }

    /** Upload captured image to the server */
    private fun uploadCapturedImage() {

        //set collector of upload if it's not already set
        if(!isUploadCollectorSet)
            setUploadImageResponseCollector()

        lifecycleScope.launch {

            //compress captured image and return it as a file
            val compressedImageFile = Compressor.compress(requireContext(), File(capturedImagePath))


            (activity as CameraActivity).viewModel._upload.uploadFile(compressedImageFile.path)
        }

    }

    /** Set a collector on upload flow to receive upload state */
    private fun setUploadImageResponseCollector() {
        isUploadCollectorSet = true
        lifecycleScope.launchWhenCreated {
            (activity as CameraActivity).viewModel._upload._uploadFlow.collect { state: ApiState ->
                when (state) {
                    is ApiState.Success<*> -> imageUploadSuccessful(state.data)
                    is ApiState.Failure -> imageUploadFailed()
                    is ApiState.Empty -> {
                    }
                    is ApiState.Loading -> imageIsUploading()
                }
            }
        }
    }

    /** Perform proper action if upload when upload is in progress */
    private fun imageIsUploading() {
        fragmentImageViewerBinding.uploadImageCoverLyt.visibility = VISIBLE
    }

    /** Perform proper action if upload was failes */
    private fun imageUploadFailed() {
        fragmentImageViewerBinding.uploadImageCoverLyt.visibility = GONE
        Toast.makeText(requireContext(), "مشکلی پیش آمد٬ لطفا مجددا تلاش کنید!", LENGTH_LONG).show()
    }

    /** Perform proper action if upload was successful */
    private fun imageUploadSuccessful(data: Any?) {

        val response: UploadSuccessResponse = Gson().fromJson(
                Gson().toJson(data),
                object : TypeToken<UploadSuccessResponse>() {}.type
        )

        fragmentImageViewerBinding.uploadImageCoverLyt.visibility = GONE
        Toast.makeText(requireContext(), response.msg, LENGTH_LONG).show()
        //return to the camera fragment
        activity?.onBackPressed()

    }


}
