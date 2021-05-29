package com.benone.mvvm2.data.http.interceptor

import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.internal.http.HttpHeaders
import okio.Buffer
import java.io.EOFException
import java.io.IOException
import java.lang.NullPointerException
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit
import kotlin.jvm.Throws

class LoggingInterceptor @JvmOverloads constructor(private val logger: Logger = Logger.DEFAULT) :
    Interceptor {

    @Volatile
    internal var level = Level.NONE

    enum class Level {
        NONE, BASIC, HEADERS, BODY
    }

    interface Logger {
        fun log(message: String)

        /**
         * 默认输出
         */
        companion object {
            val DEFAULT: Logger = object : Logger {
                override fun log(message: String) {

                }
            }
        }
    }

    fun setLevel(level: Level?): LoggingInterceptor {
        if (level == null) throw NullPointerException("level == null. Use Level. NONE instead")
        this.level = level
        return this
    }

    fun getLevel(): Level {
        return level
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if (getLevel() == Level.NONE) {
            return chain.proceed(request)
        }
        val requestBody = request.body()
        val hasRequestBody = request.body() != null
        val connection = chain.connection()
        val protocol = if (connection != null) connection.protocol() else Protocol.HTTP_1_1
        val requestStartMessage =
            "-->" + request.method() + ' '.toString() + request.url() + ' '.toString() + protocol(
                protocol
            )
        //请求方法以及url等
        logger.log(requestStartMessage)

        logger.log("-----请求头------")
        val headers = request.headers()
        run {
            var i = 0
            val count = headers.size()
            while (i < count) {
                logger.log(headers.name(i) + ": " + headers.value(i))
                i++
            }
        }

        logger.log("---请求参数---")
        val url = request.url()
        for (name in url.queryParameterNames()) {
            val sb = StringBuffer()
            sb.append(url.queryParameterValues(name))
            sb.deleteCharAt(sb.length - 1)
            sb.deleteCharAt(0)
            logger.log("$name:$sb")
        }

        logger.log("---请求体---")
        if (hasRequestBody && !bodyEncoded(headers)) {
            val buffer = Buffer()
            requestBody!!.writeTo(buffer)
            var charset = UTF8
            val contentType = requestBody.contentType()
            if (contentType != null) {
                charset = contentType.charset(UTF8)
            }
            if (isPlaintext(buffer)) {
                logger.log(buffer.readString(charset).replace("&".toRegex(), "\n"))
            }
        }
        //请求结束
        logger.log("")
        logger.log("-->END Request")
        logger.log("")

        val startNs = System.nanoTime()
        val response = chain.proceed(request)
        val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)

        val responseBody = response.body()!!
        //响应码以及url等
        logger.log(
            "<--" + response.code() + ' '.toString() + response.message() + ' '.toString() + response.request()
                .url() + " (" + tookMs + "ms" + ") "
        )

        logger.log("----响应头----")
        val responseHeaders = response.headers()
        var i = 0
        val count = responseHeaders.size()
        while(i<count){
            logger.log(responseHeaders.name(i)+":"+responseHeaders.value(i))
            i++
        }

        logger.log("---响应体---")
        if(HttpHeaders.hasBody(response)&&!bodyEncoded(responseHeaders)){
            val source = responseBody.source()
            source.request(java.lang.Long.MAX_VALUE)
            val responseBuffer = source.buffer()
            var responseCharset = UTF8
            val responseContentType = responseBody.contentType()
            if(responseContentType!=null){
                responseCharset=responseContentType.charset(UTF8)
            }
            if(isPlaintext(responseBuffer)){
                com.orhanobut.logger.Logger.json(responseBuffer.clone().readString(responseCharset))
            }
        }
        logger.log("")
        logger.log("<--END Response")

        return response

    }

    internal fun isPlaintext(buffer: Buffer): Boolean {
        try {
            val prefix = Buffer()
            val byteCount = if (buffer.size() < 64) buffer.size() else 64
            buffer.copyTo(prefix, 0, byteCount)
            for (i in 0..15) {
                if (prefix.exhausted()) {
                    break
                }
                val codePoint = prefix.readUtf8CodePoint()
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false
                }
            }
            return true
        } catch (e: EOFException) {
            return false
        }
    }

    private fun bodyEncoded(headers: Headers): Boolean {
        val contentEncoding = headers.get("Content-Encoding")
        return contentEncoding != null && !contentEncoding.equals("identity", ignoreCase = true)

    }

    private fun protocol(protocol: Protocol): String {
        return if (protocol == Protocol.HTTP_1_0) "HTTP/1.0" else "HTTP/1.1"
    }

    companion object {
        private val UTF8 = Charset.forName("UTF-8")
    }
}