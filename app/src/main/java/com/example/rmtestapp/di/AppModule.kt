package com.example.rmtestapp.di

import android.content.Context
import androidx.room.Room
import com.example.data.database.RMTestAppDatabase
import com.example.data.database.dao.CameraDao
import com.example.data.database.dao.DoorDao
import com.example.data.network.ApiService
import com.example.data.repositories.CamerasRepositoryImpl
import com.example.data.repositories.DoorsRepositoryImpl
import com.example.domain.interactors.CamerasInteractor
import com.example.domain.interactors.DoorsInteractor
import com.example.domain.repositories.CamerasRepository
import com.example.domain.repositories.DoorsRepository
import com.example.rmtestapp.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideCamerasInteractor(repository: CamerasRepository): CamerasInteractor =
        CamerasInteractor((repository))

    @Provides
    @Singleton
    fun provideCamerasRepository(apiService: ApiService, dao: CameraDao): CamerasRepository =
        CamerasRepositoryImpl(apiService, dao)

    @Provides
    @Singleton
    fun provideDoorsInteractor(repository: DoorsRepository): DoorsInteractor =
        DoorsInteractor((repository))

    @Provides
    @Singleton
    fun provideDoorsRepository(apiService: ApiService, dao: DoorDao): DoorsRepository =
        DoorsRepositoryImpl(apiService, dao)

    @Provides
    @Singleton
    fun provideCameraDao(db: RMTestAppDatabase): CameraDao = db.cameraDao()

    @Provides
    @Singleton
    fun provideDoorDao(db: RMTestAppDatabase): DoorDao = db.doorDao()

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): RMTestAppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            RMTestAppDatabase::class.java,
            "rm_app_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(BuildConfig.BASE_API_URL)
            .build()
    }
}