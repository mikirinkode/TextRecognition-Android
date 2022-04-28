package com.mikirinkode.scanner.core.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "scan_result")
@Parcelize
data class ScanResultEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var textResult: String,
    val image: ByteArray
): Parcelable
