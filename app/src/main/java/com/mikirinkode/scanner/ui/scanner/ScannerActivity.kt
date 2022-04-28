package com.mikirinkode.scanner.ui.scanner

import android.app.Activity
import android.content.*
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import com.mikirinkode.scanner.databinding.ActivityScannerBinding
import com.mikirinkode.scanner.core.domain.model.ScanResult
import com.mikirinkode.scanner.ui.main.MainViewModel
import com.mikirinkode.scanner.utils.Converter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScannerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScannerBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var scannedBitmap: Bitmap
    private lateinit var textResult: String
    private var bitmapState: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScannerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        binding.tvResult.clearFocus()
        actionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Scanner"

        // ketika user menekan gambar
        binding.ivCaptured.setOnClickListener {
            val pictureDialog = AlertDialog.Builder(this)
            pictureDialog.setTitle("Pilih Aksi:")
            val pictureDialogItem = arrayOf("Ambil Gambar dari Galeri",
                "Ambil dengan Kamera")
            pictureDialog.setItems(pictureDialogItem) { _, which ->
                when (which) {
                    0 -> checkGalleryPermission()
                    1 -> checkCameraPermission()
                }
            }
            pictureDialog.show()
        }

        if (bitmapState == null){
            binding.btnCopy.isEnabled = false
            binding.btnDetectText.isEnabled = false
            binding.btnSave.isEnabled = false
            binding.tvResult.isEnabled = false
        } else {
            binding.btnDetectText.isEnabled = true
        }

        binding.btnSave.setOnClickListener {
            if (bitmapState == null){
                Toast.makeText(this, "Anda belum memilih gambar.", Toast.LENGTH_SHORT).show()
            } else {
                saveScanResult(scannedBitmap)
            }
        }

        binding.btnDetectText.setOnClickListener {
            if (bitmapState == null){
                Toast.makeText(this, "Anda belum memilih gambar.", Toast.LENGTH_SHORT).show()
            } else {
                detectText(scannedBitmap)
            }
        }

        binding.btnCopy.setOnClickListener {
            if (textResult == "" || textResult.isEmpty()){
                Toast.makeText(this, "Text Kosong", Toast.LENGTH_SHORT).show()
            } else{
                copyToClipboard(textResult)
            }
        }

    }

    private fun checkGalleryPermission() {
        Dexter.withContext(this).withPermission(
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        ).withListener(object : PermissionListener {
            override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                gallery()
            }

            override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                Toast.makeText(
                    this@ScannerActivity,
                    "Anda belum memberikan perizinan untuk mengambil gambar.",
                    Toast.LENGTH_SHORT
                ).show()
                showRotationalDialogForPermission()
            }

            override fun onPermissionRationaleShouldBeShown(
                p0: PermissionRequest?, p1: PermissionToken?) {
                showRotationalDialogForPermission()
            }
        }).onSameThread().check()
    }

    private fun gallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }


    private fun checkCameraPermission() {
        Dexter.withContext(this)
            .withPermissions(
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA).withListener(

                object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        report?.let {
                            if (report.areAllPermissionsGranted()) {
                                camera()
                            }
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        p0: MutableList<PermissionRequest>?,
                        p1: PermissionToken?) {
                        showRotationalDialogForPermission()
                    }
                }
            ).onSameThread().check()
    }


    private fun camera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA_REQUEST_CODE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CAMERA_REQUEST_CODE -> {
                    val bitmap = data?.extras?.get("data") as Bitmap
                    scannedBitmap = bitmap
                    bitmapState = bitmap
                    binding.ivCaptured.load(bitmap) {
                        crossfade(true)
                        crossfade(1000)
                    }
                }

                GALLERY_REQUEST_CODE -> {
                    if (data?.data != null){
                        scannedBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, data.data)
                        bitmapState = MediaStore.Images.Media.getBitmap(this.contentResolver, data.data)
                    }
                    binding.ivCaptured.load(data?.data) {
                        crossfade(true)
                        crossfade(1000)
                    }
                }
            }
        }
    }

    private fun imageFromBitmap(bitmap: Bitmap): InputImage{
        return InputImage.fromBitmap(bitmap, 0)
    }


    private fun detectText(bitmap: Bitmap){
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        val image = imageFromBitmap(bitmap)

        recognizer.process(image)
            .addOnSuccessListener {
                binding.tvResult.setText(it.text)
                textResult = it.text
                binding.btnSave.isEnabled = true
                binding.btnCopy.isEnabled = true
                binding.tvResult.isEnabled = true
                Toast.makeText(this, "Teks berhasil dideteksi", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
                Toast.makeText(this, "Gagal Mendeteksi Teks", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveScanResult(scannedBitmap: Bitmap) {
        if (textResult.isNotEmpty() && textResult != ""){
            val newText = binding.tvResult.text.toString()
            val imageToByteArray = Converter.toByteArray(scannedBitmap)
            val scanResult = ScanResult(textResult = newText, image = imageToByteArray)
            viewModel.insertScanResult(scanResult)
            Toast.makeText(this, "Teks berhasil disimpan", Toast.LENGTH_SHORT).show()
            onBackPressed()
        }
    }

    private fun Context.copyToClipboard(text: CharSequence){
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("label",text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(this@ScannerActivity, "Teks Berhasil Disalin.", Toast.LENGTH_SHORT).show()
    }

    private fun showRotationalDialogForPermission() {
        AlertDialog.Builder(this)
            .setMessage("Sepertinya anda belum memberikan izin kamera atau galeri."
                    + "Cek dan aktifkan perizinan dari pengaturan aplikasi.")

            .setPositiveButton("PENGATURAN") { _, _ ->

                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)

                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }

            .setNegativeButton("BATALKAN") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    override fun onResume() {
        super.onResume()
        binding.tvResult.clearFocus()
        if (bitmapState == null){
            binding.btnCopy.isEnabled = false
            binding.btnDetectText.isEnabled = false
            binding.btnSave.isEnabled = false
        } else {
            binding.btnDetectText.isEnabled = true
        }
    }

    companion object{
        private const val CAMERA_REQUEST_CODE = 1
        private const val GALLERY_REQUEST_CODE = 2
    }
}