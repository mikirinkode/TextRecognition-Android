package com.mikirinkode.scanner.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mikirinkode.scanner.databinding.ActivityMainBinding
import com.mikirinkode.scanner.ui.scanner.ScannerActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var mainAdapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnNewScan.setOnClickListener {
                val intent = Intent(this@MainActivity, ScannerActivity::class.java)
                startActivity(intent)
            }

            mainAdapter = MainAdapter()
            rvScanResult.layoutManager = LinearLayoutManager(this@MainActivity)
            rvScanResult.setHasFixedSize(true)
            rvScanResult.adapter = mainAdapter

            viewModel.getScanHistoryList().observe(this@MainActivity) { list ->
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