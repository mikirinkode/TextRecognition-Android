package com.mikirinkode.scanner.core.domain.repository

import com.mikirinkode.scanner.core.domain.model.ScanResult
import kotlinx.coroutines.flow.Flow

interface IScannerRepository {

    fun getScanResultList(): Flow<List<ScanResult>>

    suspend fun insertScanResult(scanResult: ScanResult)

    suspend fun updateScanResult(scanResult: ScanResult)

    suspend fun removeScanResult(scanResult: ScanResult)
}