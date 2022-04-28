package com.mikirinkode.scanner.ui.detail

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.mikirinkode.scanner.core.domain.model.ScanResult
import com.mikirinkode.scanner.databinding.ActivityDetailBinding
import com.mikirinkode.scanner.ui.main.MainViewModel
import com.mikirinkode.scanner.utils.Converter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var textResult: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Detail"
        binding.tvResult.clearFocus()

        val entity = intent.getParcelableExtra<ScanResult>(EXTRA_ENTITY)

        binding.apply {
            if (entity != null) {
                setData(entity)
                btnUpdate.isEnabled = true
                btnDelete.isEnabled = true
                btnCopy.isEnabled = true
            } else {
                btnUpdate.isEnabled = false
                btnDelete.isEnabled = false
                btnCopy.isEnabled = false
            }

            btnCopy.setOnClickListener {
                if (textResult == "" || textResult.isEmpty()){
                    Toast.makeText(this@DetailActivity, "Text Kosong", Toast.LENGTH_SHORT).show()
                } else{
                    copyToClipboard(textResult)
                }
            }
        }
    }

    private fun Context.copyToClipboard(text: CharSequence){
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("label",text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(this, "Teks Berhasil Disalin.", Toast.LENGTH_SHORT).show()
    }

    private fun setData(scanResult: ScanResult) {
        binding.apply {
            textResult = scanResult.textResult
            val image = Converter.toBitmap(scanResult.image)
            tvResult.setText(scanResult.textResult)
            Glide.with(this@DetailActivity)
                .load(image)
                .into(ivCaptured)

            btnDelete.setOnClickListener {
                val alert: AlertDialog.Builder = AlertDialog.Builder(this@DetailActivity)
                alert.setTitle("Hapus hasil Scan?")
                alert.setMessage("Anda yakin ingin menghapus hasil scan ini?")
                alert.setPositiveButton("YA"
                ) { _, _ ->
                    val intent = Intent()
                    intent.putExtra(EXTRA_ENTITY, scanResult)
                    viewModel.removeScanResult(scanResult)
                    setResult(RESULT_DELETE, intent)
                    finish()
                }
                alert.setNegativeButton("TIDAK") { dialog, _ -> // close dialog
                    dialog.cancel()
                }
                alert.show()

            }
            btnUpdate.setOnClickListener {
                val newText = tvResult.text.toString()
                val newScanResult = ScanResult(id = scanResult.id, textResult = newText, image = scanResult.image)
                val intent = Intent()
                intent.putExtra(EXTRA_ENTITY, scanResult)
                viewModel.updateScanResult(newScanResult)
                setResult(RESULT_UPDATE, intent)
                finish()
            }
        }
    }


    companion object {
        const val EXTRA_ENTITY = "extra_entity"
        const val RESULT_DELETE = 301
        const val RESULT_UPDATE = 200
    }
}