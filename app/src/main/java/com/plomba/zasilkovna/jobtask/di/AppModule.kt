package com.plomba.zasilkovna.jobtask.di

import android.content.Context
import androidx.room.Room
import com.plomba.zasilkovna.jobtask.BuildConfig
import com.plomba.zasilkovna.jobtask.common.Constants
import com.plomba.zasilkovna.jobtask.data.RepoDatabase
import com.plomba.zasilkovna.jobtask.data.database.dao.RepoDetailDao
import com.plomba.zasilkovna.jobtask.data.repository.GitHubRepositoryImpl
import com.plomba.zasilkovna.jobtask.data.GitHubService
import com.plomba.zasilkovna.jobtask.domain.repository.GitHubRepository
import com.plomba.zasilkovna.jobtask.domain.usecase.http.HttpRepoListUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private val apiKey = BuildConfig.API_KEY + "7TaukrtCiyjW2jFDIb"

    @Provides
    @Singleton
    fun provideGitHubRestService(): GitHubService {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val okHttpClient = OkHttpClient.Builder().apply {
            addInterceptor(
                Interceptor { chain ->
                    val builder = chain.request().newBuilder()
                    builder.header("X-Github-Next-Global-ID", "1")
                    builder.header("User-Agent", "plomba")
                    //bad practice but in this case unavodiable as there is just one hardcoded api key
                    builder.header("Authorization", apiKey)
                    return@Interceptor chain.proceed(builder.build())
                }
            ).addInterceptor(logging)
        }.build()


        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GitHubService::class.java)
    }

    @Provides
    @Singleton
    fun provideGitHubRepository(
        service: GitHubService,
        repoDetailDao: RepoDetailDao
    ): GitHubRepository {
        return GitHubRepositoryImpl(service, repoDetailDao)
    }

    @Provides
    @Singleton
    fun provideRepoListUseCase(repository: GitHubRepository): HttpRepoListUseCase {
        return HttpRepoListUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideRepoDetailDao(@ApplicationContext applicationContext: Context): RepoDetailDao{
        val db = Room.databaseBuilder(
            applicationContext,
            RepoDatabase::class.java, "database-name"
        ).build()
        return db.repoDetailDao()
    }
}