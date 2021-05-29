package com.benone.mvvm2.data.http

import com.benone.mvvm2.BuildConfig
import com.benone.mvvm2.HttpConstants
import com.benone.mvvm2.WanApplication
import com.benone.mvvm2.data.http.interceptor.LoggingInterceptor
import com.orhanobut.logger.Logger
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

class HttpManager private constructor() {

    companion object {
        @Volatile
        private var instance: HttpManager? = null

        fun getInstance(): HttpManager {
            return instance ?: synchronized(this) {
                instance ?: HttpManager().also { instance = it }
            }
        }
    }

    internal val wanApi: WanApi by lazy {
        create(HttpConstants.BASE_URL, WanApi::class.java)
    }

    private fun <T> create(baseUrl: String, c: Class<T>): T {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(provideOKHttpClient())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(c)
    }

    private class MyLog : LoggingInterceptor.Logger {
        override fun log(message: String) {
            Logger.d(message)
        }
    }

    private fun provideOKHttpClient(): OkHttpClient {
        val loggingInterceptor = LoggingInterceptor(MyLog())
        loggingInterceptor.level =
            if (BuildConfig.DEBUG) LoggingInterceptor.Level.BODY else LoggingInterceptor.Level.NONE
        val cache = Cache(
            File(WanApplication.instance.cacheDir, HttpConstants.CACHE_NAME),
            HttpConstants.MAX_CACHE_SIZE.toLong()
        )
        val builder = OkHttpClient.Builder().connectTimeout(
            HttpConstants.NETWORK_TIME.toLong(),
            TimeUnit.SECONDS
        ).readTimeout(
            HttpConstants.NETWORK_TIME.toLong(),
            TimeUnit.SECONDS
        ).writeTimeout(
            HttpConstants.NETWORK_TIME.toLong(),
            TimeUnit.SECONDS
        ).retryOnConnectionFailure(true).addInterceptor(HeaderInteceptor())
            .addInterceptor(CacheInterceptor()).addInterceptor(CookieInteceptor())
            .addInterceptor(loggingInterceptor).cache(cache)
        return builder.build()

    }
}