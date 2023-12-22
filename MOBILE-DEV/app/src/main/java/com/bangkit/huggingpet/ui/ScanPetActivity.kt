package com.bangkit.huggingpet.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bangkit.huggingpet.R
import com.bangkit.huggingpet.adapter.ResultScan
import com.bangkit.huggingpet.databinding.ActivityScanPetBinding
import com.bangkit.huggingpet.utils.createCustomTempFile
import com.bangkit.huggingpet.utils.uriToFile
import com.bangkit.huggingpet.viewmodel.ScanViewModel
import com.google.android.material.snackbar.Snackbar
import java.io.File

class ScanPetActivity : AppCompatActivity() {
    private var _binding: ActivityScanPetBinding? = null
    private val binding get() = _binding!!

    private lateinit var scanViewModel: ScanViewModel

    private lateinit var currentPhotoPath: String

    private var results: ResultScan? = null

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityScanPetBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = resources.getString(R.string.scanpet)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        scanViewModel = ScanViewModel(applicationContext)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        binding.btnScan.setOnClickListener { startTakePhoto() }
        binding.btnGallery.setOnClickListener { startGallery() }

        binding.buttonScan.setOnClickListener {
            if (getFile != null) {
                uploadImage()
            }
        }

        scanViewModel.apply {
            loading.observe(this@ScanPetActivity) { isLoading ->
                showLoading(isLoading)
            }

            error.observe(this@ScanPetActivity) {
                if (it.isNotEmpty())
                    Snackbar.make(binding.root, getString(R.string.upload_failed), Snackbar.LENGTH_SHORT).show()
            }

            result.observe(this@ScanPetActivity) { response ->
                results = ResultScan(
                    prediction = response.prediction
                )
                binding.diseaseResult.text = results?.prediction
                binding.imageScanResult.setImageBitmap(BitmapFactory.decodeFile(getFile?.path))
                Snackbar.make(binding.root, getString(R.string.upload_success), Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun uploadImage() {
        binding.progressBar.visibility = View.VISIBLE
        scanViewModel.uploadImage()
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createCustomTempFile(application).also {
            val photoURL: Uri = FileProvider.getUriForFile(
                this@ScanPetActivity,
                "com.bangkit.huggingpet",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURL)
            launcherIntentCamera.launch(intent)
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, getString(R.string.choose_picture))
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)

            myFile.let { file ->
                getFile = file
                binding.imageScanResult.setImageBitmap(BitmapFactory.decodeFile(file.path))
            }
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri

            selectedImg.let { uri ->
                val myFile = uriToFile(uri, this@ScanPetActivity)
                getFile = myFile
                binding.imageScanResult.setImageURI(uri)
            }
        }
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
        var getFile: File? = null
    }
}
