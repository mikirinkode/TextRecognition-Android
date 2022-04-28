package com.mikirinkode.scanner.core.domain.usecase

import com.mikirinkode.scanner.core.domain.model.ScanResult
import kotlinx.coroutines.flow.Flow

interface ScannerUseCase {

    fun getScanResultList(): Flow<List<ScanResult>>

    suspend fun insertScanResult(scanResult: ScanResult)

    suspend fun updateScanResult(scanResult: ScanResult)

    suspend fun removeScanResult(scanResult: ScanResult)
}