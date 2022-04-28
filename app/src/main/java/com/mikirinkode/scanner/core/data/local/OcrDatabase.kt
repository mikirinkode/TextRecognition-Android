package com.mikirinkode.scanner.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mikirinkode.scanner.core.data.model.ScanResultEntity

@Database(
    entities = [ScanResultEntity::class],
    version = 1,
    exportSchema = false
)
abstract class OcrDatabase : RoomDatabase() {

    abstract fun orcDao(): OcrDao
}