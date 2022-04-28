package com.mikirinkode.scanner.core.data

import com.mikirinkode.scanner.core.data.local.LocalDataSource
import com.mikirinkode.scanner.core.domain.model.ScanResult
import com.mikirinkode.scanner.core.domain.repository.IScannerRepository
import com.mikirinkode.scanner.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ScannerRepository @Inject constructor(private val localDataSource: LocalDataSource) :
    IScannerRepository {
    override fun getScanResultList(): Flow<List<ScanResult>> {
        return localDataSource.getScanResultList().map {
            DataMapper.mapEntitiesToDomain(it)
        }

    }

//    override fun getScanResultItem(id: Int): Flow<ScanResult> {
//        return localDataSource.getScanResultItem(id).map {
//            DataMapper.mapEntityToDomain(it)
//        }
//    }

    override suspend fun insertScanResult(scanResult: ScanResult) {
        localDataSource.insertScanResult(DataMapper.mapDomainToEntity(scanResult))
    }

    override suspend fun updateScanResult(scanResult: ScanResult) {
        localDataSource.updateScanResult(DataMapper.mapDomainToEntity(scanResult))
    }

    override suspend fun removeScanResult(scanResult: ScanResult) {
        localDataSource.removeScanResult(DataMapper.mapDomainToEntity(scanResult))
    }
}