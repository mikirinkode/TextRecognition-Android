package com.mikirinkode.ocr.detail

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.mikirinkode.ocr.data.ScanResultEntity
import com.mikirinkode.ocr.databinding.ActivityDetailBinding
import com.mikirinkode.ocr.main.MainViewModel


class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Detail"
        binding.tvResult.clearFocus()

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        val scanId = intent.getIntExtra(EXTRA_ID, 0)
        viewModel.setSelectedId(scanId)

        viewModel.scanResultDetail.observe(this) { itemResult ->
            binding.apply {
                if (itemResult != null) {
                    setData(itemResult)
                    btnUpdate.isEnabled = true
                    btnDelete.isEnabled = true
                } else {
                    btnUpdate.isEnabled = false
                    btnDelete.isEnabled = false
                }
            }
        }
    }

    private fun setData(scanResult: ScanResultEntity) {
        binding.apply {
            tvResult.setText(scanResult.textResult)
            Glide.with(this@DetailActivity)
                .load(scanResult.image)
                .into(ivCaptured)

            btnDelete.setOnClickListener {
                val alert: AlertDialog.Builder = AlertDialog.Builder(this@DetailActivity)
                alert.setTitle("Hapus hasil Scan?")
                alert.setMessage("Anda yakin ingin menghapus hasil scan ini?")
                alert.setPositiveButton("YA"
                ) { _, _ ->
                    viewModel.removeScanResult(scanResult.id)
                    onBackPressed()
                }
                alert.setNegativeButton("TIDAK") { dialog, _ -> // close dialog
                    dialog.dismiss()
                }
                alert.show()

            }
            btnUpdate.setOnClickListener {
                val newText = tvResult.text.toString()
                Log.e("DETAIL ACTIVITY:", newText)
                val newScanResult = ScanResultEntity(id = scanResult.id, textResult = newText, image = scanResult.image)
                viewModel.updateScanResult(newScanResult)
                onBackPressed()
            }
        }
    }


    companion object {
        const val EXTRA_ID = "extra_id"
    }
}