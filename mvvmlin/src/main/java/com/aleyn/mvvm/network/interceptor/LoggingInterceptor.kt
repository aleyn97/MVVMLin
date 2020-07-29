package com.aleyn.mvvm.network.interceptor

import com.blankj.utilcode.util.JsonUtils
import com.blankj.utilcode.util.LogUtils
import okhttp3.*
import okhttp3.internal.platform.Platform
import okio.Buffer
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @auther : Aleyn
 * time   : 2020/07/24
 */
class LoggingInterceptor(
    private var printLevel: Level = Level.ALL,
    private val mPrinter: FormatPrinter = DefaultPrinter()
) : Interceptor {


    interface Logger {
        fun log(level: Int, tag: String, msg: String)

        companion object {
            val DEFAULT: Logger = object : Logger {
                override fun log(level: Int, tag: String, msg: String) {
                    Platform.get().log(level, tag + msg, null)
                }
            }
        }
    }


    enum class Level {
        /**
         * 不打印log
         */
        NONE,

        /**
         * 只打印请求信息
         */
        REQUEST,

        /**
         * 只打印响应信息
         */
        RESPONSE,

        /**
         * 所有数据全部打印
         */
        ALL
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val logRequest =
            printLevel == Level.ALL || printLevel != Level.NONE && printLevel == Level.REQUEST
        if (logRequest) {
            //打印请求信息
            if (request.body() != null && isParseable(
                    request.body()!!.contentType()
                )
            ) {
                mPrinter.printJsonRequest(
                    request,
                    parseParams(request)
                )
            } else {
                mPrinter.printFileRequest(request)
            }
        }
        val logResponse =
            printLevel == Level.ALL || printLevel != Level.NONE && printLevel == Level.RESPONSE
        val t1 = if (logResponse) System.nanoTime() else 0
        val originalResponse: Response
        originalResponse = try {
            chain.proceed(request)
        } catch (e: Exception) {
            LogUtils.w("Http Error: $e")
            throw e
        }
        val t2 = if (logResponse) System.nanoTime() else 0
        val responseBody = originalResponse.body()

        //打印响应结果
        var bodyString: String? = null
        if (responseBody != null && isParseable(responseBody.contentType())) {
            bodyString = printResult(request, originalResponse, logResponse)
        }
        if (logResponse) {
            val segmentList =
                request.url().encodedPathSegments()
            val header = originalResponse.headers().toString()
            val code = originalResponse.code()
            val isSuccessful = originalResponse.isSuccessful
            val message = originalResponse.message()
            val url = originalResponse.request().url().toString()
            if (responseBody != null && isParseable(responseBody.contentType())) {
                mPrinter.printJsonResponse(
                    TimeUnit.NANOSECONDS.toMillis(t2 - t1), isSuccessful,
                    code, header, responseBody.contentType(), bodyString, segmentList, message, url
                )
            } else {
                mPrinter.printFileResponse(
                    TimeUnit.NANOSECONDS.toMillis(t2 - t1),
                    isSuccessful, code, header, segmentList, message, url
                )
            }
        }
        return originalResponse
    }

    /**
     * 打印响应结果
     *
     * @param request     [Request]
     * @param response    [Response]
     * @param logResponse 是否打印响应结果
     * @return 解析后的响应结果
     * @throws IOException
     */
    @Throws(IOException::class)
    private fun printResult(
        request: Request,
        response: Response,
        logResponse: Boolean
    ): String? {
        return try {
            //读取服务器返回的结果
            val responseBody = response.newBuilder().build().body()
            val source = responseBody!!.source()
            source.request(Long.MAX_VALUE) // Buffer the entire body.
            val buffer = source.buffer()

            //获取content的压缩类型
            val encoding = response
                .headers()["Content-Encoding"]
            val clone = buffer.clone()

            //解析response content
            parseContent(responseBody, encoding, clone)
        } catch (e: IOException) {
            e.printStackTrace()
            "{\"error\": \"" + e.message + "\"}"
        }
    }

    /**
     * 解析服务器响应的内容
     *
     * @param responseBody [ResponseBody]
     * @param encoding     编码类型
     * @param clone        克隆后的服务器响应内容
     * @return 解析后的响应结果
     */
    private fun parseContent(
        responseBody: ResponseBody?,
        encoding: String?,
        clone: Buffer
    ): String? {
        var charset = StandardCharsets.UTF_8
        val contentType = responseBody!!.contentType()
        if (contentType != null) {
            charset = contentType.charset(charset)
        }
        return clone.readString(charset)
    }

    companion object {
        /**
         * 解析请求服务器的请求参数
         *
         * @param request [Request]
         * @return 解析后的请求信息
         * @throws UnsupportedEncodingException
         */
        @Throws(UnsupportedEncodingException::class)
        fun parseParams(request: Request): String {
            return try {
                val body = request.newBuilder().build().body() ?: return ""
                val requestbuffer = Buffer()
                body.writeTo(requestbuffer)
                var charset = Charset.forName("UTF-8")
                val contentType = body.contentType()
                if (contentType != null) {
                    charset = contentType.charset(charset)
                }
                var json = requestbuffer.readString(charset)
                if (hasUrlEncoded(json)) {
                    json = URLDecoder.decode(
                        json,
                        convertCharset(charset)
                    )
                }
                JsonUtils.formatJson(json)
            } catch (e: IOException) {
                e.printStackTrace()
                "{\"error\": \"" + e.message + "\"}"
            }
        }

        /**
         * 是否可以解析
         *
         * @param mediaType [MediaType]
         * @return `true` 为可以解析
         */
        fun isParseable(mediaType: MediaType?): Boolean {
            return if (mediaType == null) false else isText(mediaType) || isPlain(mediaType)
                    || isJson(mediaType) || isForm(mediaType)
        }

        private fun isText(mediaType: MediaType?): Boolean {
            return if (mediaType?.type() == null) false else mediaType.type() == "text"
        }

        private fun isPlain(mediaType: MediaType?): Boolean {
            return if (mediaType?.subtype() == null) false else mediaType.subtype()
                .toLowerCase(Locale.ROOT).contains("plain")
        }

        fun isJson(mediaType: MediaType?): Boolean {
            return if (mediaType?.subtype() == null) false else mediaType.subtype()
                .toLowerCase(Locale.ROOT).contains("json")
        }

        private fun isForm(mediaType: MediaType?): Boolean {
            return if (mediaType?.subtype() == null) false else mediaType.subtype()
                .toLowerCase(Locale.ROOT).contains("x-www-form-urlencoded")
        }

        private fun convertCharset(charset: Charset?): String {
            val s = charset.toString()
            val i = s.indexOf("[")
            return if (i == -1) s else s.substring(i + 1, s.length - 1)
        }

        private fun hasUrlEncoded(str: String): Boolean {
            var encode = false
            for (i in str.indices) {
                val c = str[i]
                if (c == '%' && i + 2 < str.length) {
                    // 判断是否符合urlEncode规范
                    val c1 = str[i + 1]
                    val c2 = str[i + 2]
                    if (isValidHexChar(c1) && isValidHexChar(c2)) {
                        encode = true
                        break
                    } else break
                }
            }
            return encode
        }

        private fun isValidHexChar(c: Char): Boolean =
            c in '0'..'9' || c in 'a'..'f' || c in 'A'..'F'
    }
}