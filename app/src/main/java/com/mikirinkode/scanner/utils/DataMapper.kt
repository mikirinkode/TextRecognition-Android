package com.mikirinkode.scanner.utils

import com.mikirinkode.scanner.core.data.model.ScanResultEntity
import com.mikirinkode.scanner.core.domain.model.ScanResult

object DataMapper {

    fun mapDomainToEntity(data: ScanResult): ScanResultEntity {
        return ScanResultEntity(
            data.id,
            data.textResult,
            data.image
        )
    }

    fun mapEntityToDomain(data: ScanResultEntity): ScanResult {
        return ScanResult(
            id = data.id,
            textResult = data.textResult,
            image = data.image
        )
    }

    fun mapEntitiesToDomain(data: List<ScanResultEntity>): List<ScanResult>{
        return data.map { mapEntityToDomain(it) }
    }
}