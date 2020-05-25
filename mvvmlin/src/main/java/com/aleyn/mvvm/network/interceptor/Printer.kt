package com.aleyn.mvvm.network.interceptor

import com.blankj.utilcode.util.JsonUtils
import okhttp3.FormBody
import okhttp3.Request
import okio.Buffer
import java.io.IOException

/**
 * 借鉴其他Demo里的日志打印 类
 * 当日志比较多时，有时候会出现输出不全的情况
 */
object Printer {
    private val LINE_SEPARATOR = System.getProperty("line.separator") ?: "\n"
    private val DOUBLE_SEPARATOR = LINE_SEPARATOR + LINE_SEPARATOR

    private val OMITTED_RESPONSE = arrayOf(LINE_SEPARATOR, "Omitted response body")
    private val OMITTED_REQUEST = arrayOf(LINE_SEPARATOR, "Omitted request body")

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

    private fun isEmpty(line: String) =
        line.isEmpty() || N == line || T == line || line.trim().isEmpty()

    internal fun printJsonRequest(builder: LoggingInterceptor, request: Request) {
        val requestBody = LINE_SEPARATOR + BODY_TAG + LINE_SEPARATOR + bodyToString(request)
        val tag = builder.requestTag
        if (builder.logger == null)
            log(builder.type, tag, REQUEST_UP_LINE)
        logLines(builder.type, tag, arrayOf(URL_TAG + request.url()), builder.logger, false)
        logLines(builder.type, tag, getRequest(request, builder.level), builder.logger, true)
        if (request.body() is FormBody) {
            val formBody = StringBuilder()
            val body = request.body() as FormBody?
            if (body != null && body.size() != 0) {
                for (i in 0 until body.size()) {
                    formBody.append(body.encodedName(i) + "=" + body.encodedValue(i) + "&")
                }
                formBody.delete(formBody.length - 1, formBody.length)
                logLines(builder.type, tag, arrayOf(formBody.toString()), builder.logger, true)
            }
        }
        if (builder.level == Level.BASIC || builder.level == Level.BODY) {
            logLines(
                builder.type,
                tag,
                requestBody.split(LINE_SEPARATOR.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray(),
                builder.logger,
                true
            )
        }
        if (builder.logger == null)
            log(builder.type, tag, END_LINE)
    }

    internal fun printJsonResponse(
        builder: LoggingInterceptor, chainMs: Long, isSuccessful: Boolean,
        code: Int, headers: String, bodyString: String, segments: List<String>
    ) {
        val responseBody =
            LINE_SEPARATOR!! + BODY_TAG + LINE_SEPARATOR + JsonUtils.formatJson(bodyString)
        val tag = builder.responseTag
        if (builder.logger == null)
            log(builder.type, tag, RESPONSE_UP_LINE)

        logLines(
            builder.type, tag, getResponse(
                headers, chainMs, code, isSuccessful,
                builder.level, segments
            ), builder.logger, true
        )
        if (builder.level == Level.BASIC || builder.level == Level.BODY) {
            logLines(
                builder.type,
                tag,
                responseBody.split(LINE_SEPARATOR.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray(),
                builder.logger,
                true
            )
        }
        if (builder.logger == null)
            log(builder.type, tag, END_LINE)
    }

    internal fun printFileRequest(builder: LoggingInterceptor, request: Request) {
        val tag = builder.responseTag
        if (builder.logger == null)
            log(builder.type, tag, REQUEST_UP_LINE)
        logLines(builder.type, tag, arrayOf(URL_TAG + request.url()), builder.logger, false)
        logLines(builder.type, tag, getRequest(request, builder.level), builder.logger, true)
        if (request.body() is FormBody) {
            val formBody = StringBuilder()
            val body = request.body() as FormBody?
            if (body != null && body.size() != 0) {
                for (i in 0 until body.size()) {
                    formBody.append(body.encodedName(i) + "=" + body.encodedValue(i) + "&")
                }
                formBody.delete(formBody.length - 1, formBody.length)
                logLines(builder.type, tag, arrayOf(formBody.toString()), builder.logger, true)
            }
        }
        if (builder.level == Level.BASIC || builder.level == Level.BODY) {
            logLines(builder.type, tag, OMITTED_REQUEST, builder.logger, true)
        }
        if (builder.logger == null)
            log(builder.type, tag, END_LINE)
    }

    internal fun printFileResponse(
        builder: LoggingInterceptor, chainMs: Long, isSuccessful: Boolean,
        code: Int, headers: String, segments: List<String>
    ) {
        val tag = builder.responseTag
        if (builder.logger == null)
            log(builder.type, tag, RESPONSE_UP_LINE)

        logLines(
            builder.type, tag, getResponse(
                headers, chainMs, code, isSuccessful,
                builder.level, segments
            ), builder.logger, true
        )
        logLines(builder.type, tag, OMITTED_RESPONSE, builder.logger, true)
        if (builder.logger == null)
            log(builder.type, tag, END_LINE)
    }

    private fun getRequest(request: Request, level: Level): Array<String> {
        val message: String
        val header = request.headers().toString()
        val loggableHeader = level == Level.HEADERS || level == Level.BASIC
        message = METHOD_TAG + request.method() + DOUBLE_SEPARATOR +
                when {
                    loggableHeader -> "${HEADERS_TAG}${LINE_SEPARATOR}${dotHeaders(header)}"
                    else -> ""
                }
        return message.split(LINE_SEPARATOR!!.toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
    }

    private fun getResponse(
        header: String, tookMs: Long, code: Int, isSuccessful: Boolean,
        level: Level, segments: List<String>
    ): Array<String> {
        val message: String
        val loggableHeader = level == Level.HEADERS || level == Level.BASIC
        val segmentString = slashSegments(segments)
        message =
            "${if (segmentString.isNotEmpty()) "$segmentString - " else ""}is success : $isSuccessful - $RECEIVED_TAG$tookMs ms $DOUBLE_SEPARATOR $STATUS_CODE_TAG " +
                    "$code $DOUBLE_SEPARATOR ${if (loggableHeader) HEADERS_TAG + LINE_SEPARATOR + dotHeaders(
                        header
                    ) else ""}"
        return message.split(LINE_SEPARATOR.toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
    }

    private fun slashSegments(segments: List<String>): String {
        val segmentString = StringBuilder()
        for (segment in segments) {
            segmentString.append("/").append(segment)
        }
        return segmentString.toString()
    }

    private fun dotHeaders(header: String): String {
        if (isEmpty(header)) return ""
        val headers =
            header.split(LINE_SEPARATOR!!.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
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

    private fun logLines(
        type: Int,
        tag: String,
        lines: Array<String>,
        logger: LoggingInterceptor.Logger?,
        withLineSize: Boolean
    ) {
        for (line in lines) {
            val lineLength = line.length
            val maxSize = if (withLineSize) 110 else lineLength
            for (i in 0..lineLength / maxSize) {
                val start = i * maxSize
                var end = (i + 1) * maxSize
                end = if (end > line.length) line.length else end
                if (logger == null) {
                    log(type, tag, DEFAULT_LINE + line.substring(start, end))
                } else {
                    logger.log(type, tag, line.substring(start, end))
                }
            }
        }
    }

    private fun bodyToString(request: Request): String {
        try {
            val copy = request.newBuilder().build()
            val buffer = Buffer()
            if (copy.body() == null)
                return ""
            copy.body()!!.writeTo(buffer)
            return JsonUtils.formatJson(buffer.readUtf8())
        } catch (e: IOException) {
            return "{\"err\": \"${e.message}\"}"
        }

    }

    private fun log(type: Int, tag: String, msg: String) {
        LoggingInterceptor.Logger.DEFAULT.log(type, tag, msg)
    }
}
