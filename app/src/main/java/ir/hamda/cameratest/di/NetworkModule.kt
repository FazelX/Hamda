package ir.hamda.cameratest.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.hamda.cameratest.base.Config
import ir.hamda.cameratest.network.ApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {


    @Provides
    fun provideUrl(): String {
        return Config.BASE_URL
    }

    @Provides
    fun provideRetrofit(
            baseUrl: String,
            client: OkHttpClient
    ): ApiService {

        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .client(client)
                .build()
                .create(ApiService::class.java)
    }


    @Singleton
    @Provides
    fun provideInterceptor():
            HttpLoggingInterceptor {
        return HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Singleton
    @Provides
    fun provideHttpClient(
            httpLogger: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(httpLogger).build()
    }


    @Singleton
    @Provides
    fun provideGson(): Gson {
        return Gson()
    }

}