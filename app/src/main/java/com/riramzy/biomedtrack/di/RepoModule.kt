package com.riramzy.biomedtrack.di

import com.riramzy.biomedtrack.data.repo.AuthRepoImpl
import com.riramzy.biomedtrack.data.repo.EquipmentRepoImpl
import com.riramzy.biomedtrack.data.repo.MaintenanceRepoImpl
import com.riramzy.biomedtrack.data.repo.StatusChangeRepoImpl
import com.riramzy.biomedtrack.data.repo.TaskRepoImpl
import com.riramzy.biomedtrack.domain.repo.AuthRepo
import com.riramzy.biomedtrack.domain.repo.EquipmentRepo
import com.riramzy.biomedtrack.domain.repo.MaintenanceRepo
import com.riramzy.biomedtrack.domain.repo.StatusChangeRepo
import com.riramzy.biomedtrack.domain.repo.TaskRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {
    @Binds
    @Singleton
    abstract fun bindEquipmentRepo(impl: EquipmentRepoImpl): EquipmentRepo

    @Binds
    @Singleton
    abstract fun bindMaintenanceLogRepo(impl: MaintenanceRepoImpl): MaintenanceRepo

    @Binds
    @Singleton
    abstract fun bindStatusChangeLogRepo(impl: StatusChangeRepoImpl): StatusChangeRepo

    @Binds
    @Singleton
    abstract fun bindTaskRepo(impl: TaskRepoImpl): TaskRepo

    @Binds
    @Singleton
    abstract fun bindAuthRepo(impl: AuthRepoImpl): AuthRepo
}