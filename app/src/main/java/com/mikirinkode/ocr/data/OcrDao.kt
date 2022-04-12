package com.mikirinkode.ocr.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface OcrDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScanResult(scanResultEntity: ScanResultEntity)

    @Query("SELECT * FROM scan_result")
    fun getScanResults(): LiveData<List<ScanResultEntity>>

    @Query("SELECT * FROM scan_result WHERE id = :id")
    fun getScanResultItem(id: Int): LiveData<ScanResultEntity>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateScanResult(scanResultEntity: ScanResultEntity)

    @Query("DELETE FROM scan_result WHERE id = :id")
    suspend fun removeScanResult(id: Int): Int
}