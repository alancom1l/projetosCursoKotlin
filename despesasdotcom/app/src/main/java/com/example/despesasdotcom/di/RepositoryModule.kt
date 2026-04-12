package com.example.despesasdotcom.di

import com.example.despesasdotcom.repository.FinanceRepository
import com.example.despesasdotcom.repository.InMemoryFinanceRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindFinanceRepository(
        financeRepositoryImpl: InMemoryFinanceRepository
    ): FinanceRepository
}
