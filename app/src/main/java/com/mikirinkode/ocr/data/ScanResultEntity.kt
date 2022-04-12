package com.mikirinkode.ocr.data

import android.graphics.Bitmap
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "scan_result")
@Parcelize
data class ScanResultEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var textResult: String,
    var image: Bitmap
): Parcelable
