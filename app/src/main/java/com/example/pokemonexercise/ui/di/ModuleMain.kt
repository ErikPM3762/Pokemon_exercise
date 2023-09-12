package com.example.pokemonexercise.ui.di




import com.example.data.dataSources.PokemonRemoteDataSource
import com.example.data.local.PokemonDB
import com.example.data.remote.PokemonDataSources
import com.example.data.remote.ServiceApi
import com.example.data.repository.PokemonRepository
import com.example.data.utils.Constants
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Hilt configuration file to provide and inject classes according to each need
 */
@Module (includes = [DBModule::class])
@InstallIn(SingletonComponent::class)
object ModuleMain {

    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient): ServiceApi {
        return Retrofit.Builder()
            .baseUrl(Constants.URL_BASE)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(client)
            .build()
            .create(ServiceApi::class.java)
    }

    @Singleton
    @Provides
    fun provideUserRemoteDataSource(serviceApi: ServiceApi): PokemonRemoteDataSource {
        return PokemonDataSources(serviceApi)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun providePokemonRepository(
        remoteDataSource: PokemonRemoteDataSource,
        pokemonDao: PokemonDB
    ): PokemonRepository {
        return PokemonRepository(remoteDataSource, pokemonDao.dao)
    }
}
