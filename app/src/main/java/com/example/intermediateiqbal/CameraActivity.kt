package com.example.intermediateiqbal

import android.os.Build
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import android.view.WindowInsets
import com.example.intermediateiqbal.addStory.AddStoryActivity
import com.example.intermediateiqbal.databinding.ActivityCameraBinding
import android.os.Bundle
import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.core.ImageCapture
import androidx.camera.lifecycle.ProcessCameraProvider


class CameraActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCameraBinding
    private var selectKamera: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private var photoGambar: ImageCapture? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.balikCamera.setOnClickListener {
            selectKamera = if (selectKamera == CameraSelector.DEFAULT_BACK_CAMERA)
            {
                CameraSelector.DEFAULT_FRONT_CAMERA
            }
            else
            {
                CameraSelector.DEFAULT_BACK_CAMERA
            }
            camera()
        }

        binding.photo.setOnClickListener {
            ambilPhoto()
        }
    }

    public override fun onResume() {
        super.onResume()
        hideSystemUI()
        camera()
    }

    private fun hideSystemUI() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }



    private fun ambilPhoto() {
        val imageCapture = photoGambar ?: return

        val photoFile = Constants.createFile(application)

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Toast.makeText(
                        this@CameraActivity,
                        "gagal mengambil gambar",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val intent = Intent()
                    intent.putExtra("picture", photoFile)
                    intent.putExtra(
                        "isBackCamera",
                        selectKamera == CameraSelector.DEFAULT_BACK_CAMERA
                    )
                    setResult(AddStoryActivity.CAMERA_X_RESULT, intent)
                    finish()
                }
            }
        )
    }

    private fun camera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.placeView.surfaceProvider)
                }

            photoGambar = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    selectKamera,
                    preview,
                    photoGambar
                )
            } catch (exc: Exception) {
                Toast.makeText(
                    this@CameraActivity,
                    "Gagal menampilkan gambar",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }, ContextCompat.getMainExecutor(this))

    }


}