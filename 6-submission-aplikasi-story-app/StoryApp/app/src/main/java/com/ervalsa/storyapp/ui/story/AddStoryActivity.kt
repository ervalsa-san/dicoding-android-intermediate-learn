package com.ervalsa.storyapp.ui.story

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.ervalsa.storyapp.StoryViewModelFactory
import com.ervalsa.storyapp.ViewModelFactory
import com.ervalsa.storyapp.data.Result
import com.ervalsa.storyapp.data.local.datastore.UserPreference
import com.ervalsa.storyapp.data.remote.response.story.FileUploadResponse
import com.ervalsa.storyapp.data.remote.retrofit.ApiConfig
import com.ervalsa.storyapp.databinding.ActivityAddStoryBinding
import com.ervalsa.storyapp.ui.main.MainActivity
import com.ervalsa.storyapp.ui.main.MainViewModel
import com.ervalsa.storyapp.ui.main.StoryViewModel
import com.ervalsa.storyapp.utils.reduceFileImage
import com.ervalsa.storyapp.utils.rotateBitmap
import com.ervalsa.storyapp.utils.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class AddStoryActivity : AppCompatActivity() {

    companion object {
        const val CAMERA_X_RESULT = 200

        private val REQUIRED_PERMISSION = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSION = 10
    }

    private lateinit var binding: ActivityAddStoryBinding

    private var getFile: File? = null
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()

        val actionBar = supportActionBar
        actionBar?.title = ""
        actionBar?.elevation = 0f

        if (!allPermissionGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSION,
                REQUEST_CODE_PERMISSION
            )
        }

        binding.btnCamera.setOnClickListener {
            val intent = Intent(this@AddStoryActivity, CameraActivity::class.java)
            launcherIntentCameraX.launch(intent)
        }

        binding.btnGallery.setOnClickListener {
            val intent = Intent()
            intent.action = ACTION_GET_CONTENT
            intent.type = "image/"
            val chooser = Intent.createChooser(intent, "Pilih gambar")
            launcherIntentGallery.launch(chooser)
        }

        binding.btnPostStory.setOnClickListener {
            if (getFile != null) {
                val file = reduceFileImage(getFile as File)
                val description = binding.edtDescription.text.toString().toRequestBody("text/plain".toMediaType())
                val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "photo",
                    file.name,
                    requestImageFile
                )

                mainViewModel.getUser().observe(this) { user ->
                    if (user.isLogin) {
                        val client = ApiConfig.getApiService().addStory(
                            "Bearer ${user.token}",
                            imageMultipart,
                            description
                        )
                        showLoading(true)
                        client.enqueue(object : Callback<FileUploadResponse> {
                            override fun onResponse(
                                call: Call<FileUploadResponse>,
                                response: Response<FileUploadResponse>
                            ) {
                                if (response.isSuccessful) {
                                    val responseBody = response.body()
                                    if (responseBody != null && !responseBody.error) {
                                        showLoading(false)
                                        Toast.makeText(
                                            this@AddStoryActivity,
                                            responseBody.message,
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        val intent = Intent(this@AddStoryActivity, MainActivity::class.java)
                                        startActivity(intent)
                                    } else {
                                        showLoading(false)
                                        Toast.makeText(
                                            this@AddStoryActivity,
                                            response.message(),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }

                            override fun onFailure(call: Call<FileUploadResponse>, t: Throwable) {
                                showLoading(false)
                                Toast.makeText(
                                    this@AddStoryActivity,
                                    "Gagal instance Retrofit",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        })
                    }
                }
            } else {
                Toast.makeText(
                    this@AddStoryActivity,
                    "Silahkan masukkan gambar terlebih dahulu",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
       if (it.resultCode == CAMERA_X_RESULT) {
           val myFile = it.data?.getSerializableExtra("picture") as File
           val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

           getFile = myFile
           val result = rotateBitmap(
               BitmapFactory.decodeFile(getFile?.path),
               isBackCamera
           )
           binding.imgPhotoStory.setImageBitmap(result)
       }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this)
            getFile = myFile
            binding.imgPhotoStory.setImageURI(selectedImg)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (!allPermissionGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan izin",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionGranted() = REQUIRED_PERMISSION.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(
                UserPreference.getInstance(dataStore))
        )[MainViewModel::class.java]
    }

}