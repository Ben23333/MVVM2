package com.benone.mvvm2.data.http.interceptor

import com.benone.mvvm2.WanApplication
import com.benone.mvvm2.utils.NetUtils
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response

/**
 *
 */
class CacheInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        if (!NetUtils.isConnected(WanApplication.instance)) {
            request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build()
        }
        val response = chain.proceed(request)
        if (NetUtils.isConnected(WanApplication.instance)) {
            val maxAge = 60 * 3
            response.newBuilder().removeHeader("Pragma")
                .header("Cache-Control", "public, max-age=$maxAge").build()
        } else {
            val maxStale = 60 * 60 * 24 * 7
            response.newBuilder().removeHeader("Pragma")
                .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale").build()
        }
        return response

    }
}