package com.mikirinkode.scanner.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.mikirinkode.scanner.core.domain.model.ScanResult
import com.mikirinkode.scanner.core.domain.usecase.ScannerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val scannerUseCase: ScannerUseCase) : ViewModel() {

//    private val selectedScanResultId = MutableLiveData<Int>()
//
//    fun setSelectedId(id: Int){
//        this.selectedScanResultId.value = id
//    }
//
//    var scanResultDetail: LiveData<ScanResult> =
//        Transformations.switchMap(selectedScanResultId) { id ->
//            scannerUseCase.getScanResultItem(id).asLiveData()
//        }

//    fun getScanResultItem(id: Int): LiveData<ScanResult>{
//        return scannerUseCase.getScanResultItem(id).asLiveData()
//    }

    fun getScanHistoryList(): LiveData<List<ScanResult>> {
        return scannerUseCase.getScanResultList().asLiveData()
    }


    fun insertScanResult(scanResult: ScanResult) {
        viewModelScope.launch {
            scannerUseCase.insertScanResult(scanResult)
        }
    }

    fun updateScanResult(scanResult: ScanResult) {
        viewModelScope.launch {
            scannerUseCase.updateScanResult(scanResult)
        }
    }

    fun removeScanResult(scanResult: ScanResult) {
        viewModelScope.launch { scannerUseCase.removeScanResult(scanResult) }
    }
}

