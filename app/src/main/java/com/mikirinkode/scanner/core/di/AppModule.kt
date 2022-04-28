package com.mikirinkode.scanner.core.di

import android.content.Context
import androidx.room.Room
import com.mikirinkode.scanner.BuildConfig
import com.mikirinkode.scanner.core.data.ScannerRepository
import com.mikirinkode.scanner.core.data.local.LocalDataSource
import com.mikirinkode.scanner.core.data.local.OcrDao
import com.mikirinkode.scanner.core.data.local.OcrDatabase
import com.mikirinkode.scanner.core.domain.repository.IScannerRepository
import com.mikirinkode.scanner.core.domain.usecase.ScannerInteractor
import com.mikirinkode.scanner.core.domain.usecase.ScannerUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext app: Context): OcrDatabase {
        val passphrase: ByteArray = SQLiteDatabase.getBytes(BuildConfig.PASSPHRASE.toCharArray())
        val factory = SupportFactory(passphrase)
        return Room.databaseBuilder(app, OcrDatabase::class.java, "ocr_database")
            .openHelperFactory(factory)
            .build()
    }

    @Singleton
    @Provides
    fun provideDao(database: OcrDatabase): OcrDao {
        return database.orcDao()
    }

    @Singleton
    @Provides
    fun provideRepository(dao: OcrDao): IScannerRepository {
        val localDataSource = LocalDataSource(dao)
        return ScannerRepository(localDataSource)
    }

    @Singleton
    @Provides
    fun provideScannerUseCase(repository: IScannerRepository): ScannerUseCase {
        return ScannerInteractor(repository)
    }
}