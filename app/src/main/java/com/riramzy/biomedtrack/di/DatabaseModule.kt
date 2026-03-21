package com.riramzy.biomedtrack.di

import android.content.Context
import androidx.room.Room
import com.riramzy.biomedtrack.data.local.database.BioMedDatabase
import com.riramzy.biomedtrack.data.local.database.RoomConverters
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): BioMedDatabase {
        return Room.databaseBuilder(
            context,
            BioMedDatabase::class.java,
            "biomed_database"
        )
            .addTypeConverter(RoomConverters())
            .build()
    }

    @Provides
    fun provideEquipmentDao(database: BioMedDatabase) = database.equipmentDao()

    @Provides
    fun provideMaintenanceLogDao(database: BioMedDatabase) = database.maintenanceLogDao()

    @Provides
    fun provideStatusChangeLogDao(database: BioMedDatabase) = database.statusChangeLogDao()

    @Provides
    fun provideTaskDao(database: BioMedDatabase) = database.taskDao()

    @Provides
    fun provideTechnicianDao(database: BioMedDatabase) = database.technicianDao()
}