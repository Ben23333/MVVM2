package com.benone.mvvm2.data.http.interceptor

import com.benone.mvvm2.HttpConstants
import com.benone.mvvm2.utils.PreferenceUtils
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor:Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder = request.newBuilder()
        builder.addHeader("Content-type","application/json; charset=utf-8")

        val host= request.url().host()
        val requestUrl = request.url().toString()
        val loginRequired = HttpConstants.LOGIN_REQUIRED_URLS.firstOrNull { url-> requestUrl.contains(url)}

        if(host.isNotEmpty()&&!loginRequired.isNullOrEmpty()){
            val spHost:String by PreferenceUtils(host,"")
            val cookie:String = if(spHost.isNotEmpty()) spHost else ""
            if(cookie.isNotEmpty()){
                builder.addHeader(HttpConstants.COOKIE_HEADER_REQUEST,cookie)
            }
        }
        return chain.proceed(builder.build())
    }
}