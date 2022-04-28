package com.mikirinkode.scanner.core.data.local

import com.mikirinkode.scanner.core.data.model.ScanResultEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val dao: OcrDao) {
    fun getScanResultList(): Flow<List<ScanResultEntity>> = dao.getScanResults()

//    fun getScanResultItem(id: Int): Flow<ScanResultEntity> = dao.getScanResultItem(id)

    suspend fun insertScanResult(data: ScanResultEntity) = dao.insertScanResult(data)

    suspend fun updateScanResult(data: ScanResultEntity) = dao.updateScanResult(data)

    suspend fun removeScanResult(data: ScanResultEntity) = dao.removeScanResult(data)
}