package com.example.myapplication.di

import android.content.Context
import androidx.room.Room
import com.example.myapplication.data.local.AppDatabase
import com.example.myapplication.data.remote.Api
import com.example.myapplication.data.repository.NewsRepositoryImpl
import com.example.myapplication.domain.repository.NewsRepository
import com.example.myapplication.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "database"
    ).fallbackToDestructiveMigration().build()


    @Provides
    @Singleton
    fun provideHistoryDao(database: AppDatabase) = database.historyDao()

    @Provides
    @Singleton
    fun provideNewsDao(database: AppDatabase) = database.newsDao()


    @Provides
    @Singleton
    fun provideOkHttpClient() = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val original = chain.request()
            val requestBuilderForToken = original.newBuilder()
                .method(original.method, original.body)
//                .header("apiKey", Constants.NEWS_API)
            val requestToken = requestBuilderForToken.build()
            chain.proceed(requestToken)
        }.build()


    @Provides
    @Singleton
    fun provideRetrofitInstance(
        okHttpClient: OkHttpClient,
    ) = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Singleton
    @Provides
    fun provideApi(retrofit: Retrofit) = retrofit.create(Api::class.java)


    @Singleton
    @Provides
    fun provideNewsRepository(api: Api): NewsRepository = NewsRepositoryImpl(api)


}