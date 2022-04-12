package com.mikirinkode.ocr.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.mikirinkode.ocr.data.OcrDao
import com.mikirinkode.ocr.data.OcrDatabase
import com.mikirinkode.ocr.data.ScanResultEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private var ocrDao: OcrDao?
    private var ocrDb: OcrDatabase? = OcrDatabase.getDatabase(application)
    private val selectedScanResultId = MutableLiveData<Int>()

    init {
        ocrDao = ocrDb?.orcDao()
    }

    fun setSelectedId(id: Int){
        this.selectedScanResultId.value = id
    }

    var scanResultDetail: LiveData<ScanResultEntity> =
        Transformations.switchMap(selectedScanResultId) { id ->
            ocrDao?.getScanResultItem(id)
        }

    fun getScanHistoryList(): LiveData<List<ScanResultEntity>>? {
        return ocrDao?.getScanResults()
    }


    fun insertScanResult(scanResultEntity: ScanResultEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            ocrDao?.insertScanResult(scanResultEntity)
        }
    }

    fun updateScanResult(scanResultEntity: ScanResultEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            ocrDao?.updateScanResult(scanResultEntity)
        }
    }

    fun removeScanResult(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            ocrDao?.removeScanResult(id)
        }
    }
}

