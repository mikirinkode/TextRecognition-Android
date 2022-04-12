package com.mikirinkode.ocr.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mikirinkode.ocr.utils.Converter

@Database(
    entities = [ScanResultEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converter::class)
abstract class OcrDatabase : RoomDatabase() {
    companion object {
        private var INSTANCE: OcrDatabase? = null

        fun getDatabase(context: Context): OcrDatabase? {
            if (INSTANCE == null) {
                synchronized(OcrDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        OcrDatabase::class.java,
                        "ocr_database"
                    ).build()
                }
            }
            return INSTANCE
        }
    }

    abstract fun orcDao(): OcrDao
}