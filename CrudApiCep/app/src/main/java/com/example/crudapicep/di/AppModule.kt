package com.example.crudapicep.di

import android.content.Context
import com.example.crudapicep.data.local.ContatoDao
import com.example.crudapicep.data.local.ContatoDatabase
import com.example.crudapicep.network.ViaCepApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ContatoDatabase {
        return ContatoDatabase.getDatabase(context)
    }

    @Provides
    fun provideContatoDao(database: ContatoDatabase): ContatoDao {
        return database.contatoDao()
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://viacep.com.br/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideViaCepApi(retrofit: Retrofit): ViaCepApi {
        return retrofit.create(ViaCepApi::class.java)
    }
}