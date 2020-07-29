package com.aleyn.mvvm.network.interceptor

import android.text.TextUtils
import com.blankj.utilcode.util.JsonUtils
import okhttp3.MediaType
import okhttp3.Request
import okhttp3.internal.platform.Platform.INFO

/**
 * @auther : Aleyn
 * time   : 2020/07/24
 */
class DefaultPrinter(private val logger: LoggingInterceptor.Logger = LoggingInterceptor.Logger.DEFAULT) :
    FormatPrinter {

    /**
     * @param request
     * @param bodyString
     */
    override fun printJsonRequest(
        request: Request?,
        bodyString: String?
    ) {
        val requestBody = LINE_SEPARATOR + BODY_TAG + LINE_SEPARATOR + bodyString
        val tag = getTag(true)
        debugInfo(tag, REQUEST_UP_LINE)
        logLines(tag, arrayOf(URL_TAG + request!!.url()), false)
        logLines(tag, getRequest(request), true)
        logLines(tag, requestBody.split(LINE_SEPARATOR.toRegex()).toTypedArray(), true)
        debugInfo(tag, END_LINE)
    }

    /**
     * @param request
     */
    override fun printFileRequest(request: Request?) {
        val tag = getTag(true)
        debugInfo(tag, REQUEST_UP_LINE)
        logLines(tag, arrayOf(URL_TAG + request!!.url()), false)
        logLines(tag, getRequest(request), true)
        logLines(tag, OMITTED_REQUEST, true)
        debugInfo(tag, END_LINE)
    }

    /**
     *
     * @param chainMs      服务器响应耗时(单位毫秒)
     * @param isSuccessful 请求是否成功
     * @param code         响应码
     * @param headers      请求头
     * @param contentType  服务器返回数据的数据类型
     * @param bodyString      服务器返回的数据(已解析)
     * @param segments     域名后面的资源地址
     * @param message      响应信息
     * @param responseUrl  请求地址
     */
    override fun printJsonResponse(
        chainMs: Long,
        isSuccessful: Boolean,
        code: Int,
        headers: String?,
        contentType: MediaType?,
        bodyString: String?,
        segments: List<String?>?,
        message: String?,
        responseUrl: String?
    ) {
        var tempString = bodyString
        tempString = if (LoggingInterceptor.isJson(contentType)) JsonUtils.formatJson(tempString)
        else tempString
        val responseBody = LINE_SEPARATOR + BODY_TAG + LINE_SEPARATOR + tempString
        val tag = getTag(false)
        val urlLine = arrayOf(URL_TAG + responseUrl, N)
        debugInfo(tag, RESPONSE_UP_LINE)
        logLines(tag, urlLine, true)
        logLines(tag, getResponse(headers, chainMs, code, isSuccessful, segments, message), true)
        logLines(tag, responseBody.split(LINE_SEPARATOR.toRegex()).toTypedArray(), true)
        debugInfo(tag, END_LINE)
    }

    /**
     *
     * @param chainMs      服务器响应耗时(单位毫秒)
     * @param isSuccessful 请求是否成功
     * @param code         响应码
     * @param headers      请求头
     * @param segments     域名后面的资源地址
     * @param message      响应信息
     * @param responseUrl  请求地址
     */
    override fun printFileResponse(
        chainMs: Long,
        isSuccessful: Boolean,
        code: Int,
        headers: String?,
        segments: List<String?>?,
        message: String?,
        responseUrl: String?
    ) {
        val tag = getTag(false)
        val urlLine = arrayOf(URL_TAG + responseUrl, N)
        debugInfo(tag, RESPONSE_UP_LINE)
        logLines(tag, urlLine, true)
        logLines(tag, getResponse(headers, chainMs, code, isSuccessful, segments, message), true)
        logLines(tag, OMITTED_RESPONSE, true)
        debugInfo(tag, END_LINE)
    }

    companion object {
        private val LINE_SEPARATOR = System.getProperty("line.separator") ?: "\n"
        private val DOUBLE_SEPARATOR =
            LINE_SEPARATOR + LINE_SEPARATOR
        private val OMITTED_RESPONSE = arrayOf(
            LINE_SEPARATOR,
            "Omitted response body"
        )
        private val OMITTED_REQUEST = arrayOf(
            LINE_SEPARATOR,
            "Omitted request body"
        )
        private const val N = "\n"
        private const val T = "\t"
        private const val REQUEST_UP_LINE =
            "┌────── Request ────────────────────────────────────────────────────────────────────────"
        private const val END_LINE =
            "└───────────────────────────────────────────────────────────────────────────────────────"
        private const val RESPONSE_UP_LINE =
            "┌────── Response ───────────────────────────────────────────────────────────────────────"
        private const val BODY_TAG = "Body:"
        private const val URL_TAG = "URL: "
        private const val METHOD_TAG = "Method: @"
        private const val HEADERS_TAG = "Headers:"
        private const val STATUS_CODE_TAG = "Status Code: "
        private const val RECEIVED_TAG = "Received in: "
        private const val CORNER_UP = "┌ "
        private const val CORNER_BOTTOM = "└ "
        private const val CENTER_LINE = "├ "
        private const val DEFAULT_LINE = "│ "
        private fun isEmpty(line: String?): Boolean {
            return TextUtils.isEmpty(line) || N == line || T == line || TextUtils.isEmpty(
                line!!.trim { it <= ' ' }
            )
        }
    }

    /**
     * 对 `lines` 中的信息进行逐行打印
     *
     * @param tag
     * @param lines
     * @param withLineSize 为 `true` 时, 每行的信息长度不会超过110, 超过则自动换行
     */
    private fun logLines(tag: String, lines: Array<String>, withLineSize: Boolean) {
        for (line in lines) {
            val lineLength = line.length
            val maxSize = if (withLineSize) 110 else lineLength
            for (i in 0..lineLength / maxSize) {
                val start = i * maxSize
                var end = (i + 1) * maxSize
                end = if (end > line.length) line.length else end
                debugInfo(tag, DEFAULT_LINE + line.substring(start, end))
            }
        }
    }

    private fun getRequest(request: Request?): Array<String> {
        val log: String
        val header = request!!.headers().toString()
        log = METHOD_TAG + request.method() + DOUBLE_SEPARATOR + if (isEmpty(header)) ""
        else HEADERS_TAG + LINE_SEPARATOR + dotHeaders(header)
        return log.split(LINE_SEPARATOR.toRegex()).toTypedArray()
    }

    private fun getResponse(
        header: String?, tookMs: Long, code: Int, isSuccessful: Boolean,
        segments: List<String?>?, message: String?
    ): Array<String> {
        val log: String
        val segmentString =
            slashSegments(segments)
        log =
            ((if (!TextUtils.isEmpty(segmentString)) "$segmentString - " else "") + "is success : "
                    + isSuccessful + " - " + RECEIVED_TAG + tookMs + "ms" + DOUBLE_SEPARATOR + STATUS_CODE_TAG +
                    code + " / " + message + DOUBLE_SEPARATOR + if (isEmpty(
                    header
                )
            ) "" else HEADERS_TAG + LINE_SEPARATOR +
                    dotHeaders(header))
        return log.split(LINE_SEPARATOR.toRegex()).toTypedArray()
    }

    private fun slashSegments(segments: List<String?>?): String {
        val segmentString = StringBuilder()
        for (segment in segments!!) {
            segmentString.append("/").append(segment)
        }
        return segmentString.toString()
    }

    /**
     * 对 `header` 按规定的格式进行处理
     *
     * @param header
     * @return
     */
    private fun dotHeaders(header: String?): String {
        val headers =
            header!!.split(LINE_SEPARATOR.toRegex()).toTypedArray()
        val builder = StringBuilder()
        var tag = "─ "
        if (headers.size > 1) {
            for (i in headers.indices) {
                tag = when (i) {
                    0 -> CORNER_UP
                    headers.size - 1 -> CORNER_BOTTOM
                    else -> CENTER_LINE
                }
                builder.append(tag).append(headers[i]).append("\n")
            }
        } else {
            for (item in headers) {
                builder.append(tag).append(item).append("\n")
            }
        }
        return builder.toString()
    }

    private fun getTag(isRequest: Boolean): String {
        return if (isRequest) {
            "-Request  "
        } else {
            "-Response  "
        }
    }

    private fun debugInfo(tag: String, msg: String) {
        if (TextUtils.isEmpty(msg)) return
        logger.log(INFO, tag, msg)
    }
}