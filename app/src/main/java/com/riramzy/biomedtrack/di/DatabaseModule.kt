package com.riramzy.biomedtrack.di

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
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
    fun providesGson(): Gson = Gson()

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
        converters: RoomConverters
    ): BioMedDatabase {
        return Room.databaseBuilder(
            context,
            BioMedDatabase::class.java,
            "biomed_database"
        )
            .addTypeConverter(converters)
            .fallbackToDestructiveMigration(true)
            .build()
    }

    @Provides
    fun provideRoomConverters(gson: Gson): RoomConverters {
        return RoomConverters(gson)
    }

    @Provides
    fun provideEquipmentDao(database: BioMedDatabase) = database.equipmentDao()

    @Provides
    fun provideDepartmentDao(database: BioMedDatabase) = database.departmentDao()

    @Provides
    fun provideMaintenanceLogDao(database: BioMedDatabase) = database.maintenanceLogDao()

    @Provides
    fun provideStatusChangeLogDao(database: BioMedDatabase) = database.statusChangeLogDao()

    @Provides
    fun provideTaskDao(database: BioMedDatabase) = database.taskDao()

    @Provides
    fun provideTechnicianDao(database: BioMedDatabase) = database.technicianDao()
}