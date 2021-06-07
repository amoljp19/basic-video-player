package com.softaai.basicvideoplayer.videoplayer;

import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.AspectRatio
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import com.softaai.basicvideoplayer.R
import com.softaai.basicvideoplayer.databinding.ActivityCaptureBinding
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class CaptureActivity: AppCompatActivity(){

    lateinit var activityCaptureBinding: ActivityCaptureBinding

    private val cameraViewModel: CameraViewModel by viewModels()

    private var lensFacing: Int = CameraSelector.LENS_FACING_BACK

    private var preview: Preview? = null
    private var camera: Camera? = null

    lateinit var permissionsHelper: PermissionsHelper

    private var permissionsGranted: Boolean = false

    companion object {
        private const val RATIO_4_3_VALUE = 4.0 / 3.0
        private const val RATIO_16_9_VALUE = 16.0 / 9.0
        private const val TAG: String = "CameraX"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityCaptureBinding = DataBindingUtil.setContentView(this, R.layout.activity_capture)

        activityCaptureBinding.lifecycleOwner = this

        activityCaptureBinding.layoutCameraControlUi.viewModel = cameraViewModel

        activityCaptureBinding.layoutCameraControlUi.cameraCaptureButton.setOnClickListener {
            permissionsHelper = PermissionsHelper(this)
            permissionsGranted = permissionsHelper.hasCameraPermission()
        }



    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (permissionsHelper.resultGranted(requestCode, permissions, grantResults)) {
            permissionsGranted = true
            startCamera()
        }
    }



    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun startCamera() {
        activityCaptureBinding.viewFinder.post {
            bindCameraUseCases()
        }
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun bindCameraUseCases() {

        val metrics = DisplayMetrics().also { activityCaptureBinding.viewFinder.display.getRealMetrics(it) }
        Log.d(TAG, "Screen metrics: ${metrics.widthPixels} x ${metrics.heightPixels}")

        val screenAspectRatio = aspectRatio(metrics.widthPixels, metrics.heightPixels)
        Log.d(TAG, "Preview aspect ratio: $screenAspectRatio")

        val rotation = activityCaptureBinding.viewFinder.display.rotation


        val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener(Runnable {

            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            preview = Preview.Builder()
                .setTargetAspectRatio(screenAspectRatio)
//                .setTargetResolution(Size(metrics.widthPixels/2, metrics.heightPixels/2))
                // Set initial target rotation
                .setTargetRotation(rotation)
                .build()

            cameraProvider.unbindAll()

            try {

                camera =
                    cameraProvider.bindToLifecycle(
                        this as LifecycleOwner,
                        cameraSelector,
                        preview
                    )

                preview?.setSurfaceProvider(activityCaptureBinding.viewFinder.surfaceProvider)
            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }


    private fun aspectRatio(width: Int, height: Int): Int {
        val previewRatio = max(width, height).toDouble() / min(width, height)
        if (abs(previewRatio - RATIO_4_3_VALUE) <= abs(previewRatio - RATIO_16_9_VALUE)) {
            return AspectRatio.RATIO_4_3
        }
        return AspectRatio.RATIO_16_9
    }

}
