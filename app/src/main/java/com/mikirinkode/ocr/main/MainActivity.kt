package com.mikirinkode.ocr.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mikirinkode.ocr.scanner.ScannerActivity
import com.mikirinkode.ocr.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var mainAdapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        binding.apply {
            btnNewScan.setOnClickListener {
                val intent = Intent(this@MainActivity, ScannerActivity::class.java)
                startActivity(intent)
            }

            mainAdapter = MainAdapter()
            rvScanResult.layoutManager = LinearLayoutManager(this@MainActivity)
            rvScanResult.setHasFixedSize(true)
            rvScanResult.adapter = mainAdapter

            viewModel.getScanHistoryList()?.observe(this@MainActivity) { list ->
                if (list != null) {
                    mainAdapter.setData(list)
                    mainAdapter.notifyDataSetChanged()
                    if (list.isEmpty()) {
                        binding.labelNoData.visibility = View.VISIBLE
                    } else {
                        binding.labelNoData.visibility = View.GONE
                    }
                }
            }
        }
    }
}