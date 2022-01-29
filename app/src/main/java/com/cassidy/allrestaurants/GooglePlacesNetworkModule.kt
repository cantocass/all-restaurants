package com.cassidy.allrestaurants

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GooglePlacesNetworkModule {

//    "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522%2C151.1957362&radius=1500&type=restaurant&keyword=cruise&key=YOUR_API_KEY")
    const val BASE_URL = "https://maps.googleapis.com/maps/api/place/"


    @Provides
    @Named("googleMapsApi")
    fun provideApiKeyInterceptor() : Interceptor {
        return Interceptor {
            val original = it.request()
            val key = BuildConfig.MAPS_API_KEY
            val url = original.url().newBuilder().addQueryParameter("key", key).build()
            it.proceed(original.newBuilder().url(url).build())
        }
    }

    @Singleton
    @Provides
    fun provideHttpClient(@Named("googleMapsApi") keyInterceptor: Interceptor): OkHttpClient {
        return OkHttpClient
            .Builder()
            .addInterceptor(keyInterceptor)
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideConverterFactory(): GsonConverterFactory =
        GsonConverterFactory.create()

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Singleton
    @Provides
    fun provideGooglePlacesApi(retrofit: Retrofit): GooglePlacesApi = retrofit.create(GooglePlacesApi::class.java)

}