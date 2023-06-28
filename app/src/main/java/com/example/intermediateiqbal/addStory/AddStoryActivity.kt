package com.example.intermediateiqbal.addStory

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.intermediateiqbal.CameraActivity
import com.example.intermediateiqbal.Constants.reduceFileImage
import com.example.intermediateiqbal.Constants.rotateFile
import com.example.intermediateiqbal.Constants.uriToFile
import com.example.intermediateiqbal.UserPreferences
import com.example.intermediateiqbal.databinding.ActivityAddStoryBinding
import com.example.intermediateiqbal.viewmodel.AddStoryViewModel
import com.example.intermediateiqbal.viewmodel.ViewModelFactory
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAddStoryBinding
    private lateinit var multipartBody: MultipartBody.Part
    private lateinit var description : RequestBody
    private var status : Boolean = false
    private lateinit var addStoryViewModel: AddStoryViewModel
    private lateinit var token : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        addStoryViewModel = ViewModelProvider(this,
            ViewModelFactory(UserPreferences.getInstance(dataStore))
        )[AddStoryViewModel::class.java]

        token = intent.getStringExtra(EXTRA_ADD_TOKEN)!!

        binding.btnAddCamera.setOnClickListener {
            starCameraX()
        }

        binding.btnAddGalery.setOnClickListener {
            startGallery()
        }
        binding.buttonAdd.setOnClickListener {
            uploadStories()
        }

        addStoryViewModel.isUpload.observe(this){
            if (it){
                Toast.makeText(this, "Berhasil Upload", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

    }

    private fun uploadStories(){
        val desc = binding.edAddDescription.text.toString()
        description = desc.toRequestBody("text/plain".toMediaType())

        if (status){
            addStoryViewModel.uploadStory(multipartBody, description, token)
        }
    }


    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Pilih gambar")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val myFile = uriToFile(uri, this@AddStoryActivity)
                val reduce = reduceFileImage(myFile)
                val requestImageFile = reduce.asRequestBody("image/jpeg".toMediaTypeOrNull())
                multipartBody =  MultipartBody.Part.createFormData("photo", "photo", requestImageFile)
                status = true
                binding.tvAddImg.setImageURI(uri)
            }
        }
    }



    private fun starCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.data?.getSerializableExtra("picture", File::class.java)
            } else {
                @Suppress("DEPRECATION")
                it.data?.getSerializableExtra("picture")
            } as? File

            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean
            myFile?.let { file ->
                rotateFile(file, isBackCamera)
                val reduceFile = reduceFileImage(file)
                val requestImageFile = reduceFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
                multipartBody =  MultipartBody.Part.createFormData("photo", "photo", requestImageFile)
                status = true
                binding.tvAddImg.setImageBitmap(BitmapFactory.decodeFile(reduceFile.path))
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapat Permission",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }


    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
        const val EXTRA_ADD_TOKEN = "addToken"
    }

}