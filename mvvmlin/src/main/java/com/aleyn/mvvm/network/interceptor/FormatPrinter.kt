package com.aleyn.mvvm.network.interceptor

import okhttp3.MediaType
import okhttp3.Request

/**
 * @auther : Aleyn
 * time   : 2020/07/24
 */
interface FormatPrinter {


    fun printJsonRequest(request: Request?, bodyString: String?)


    fun printFileRequest(request: Request?)

    /**
     *
     * @param chainMs      服务器响应耗时(单位毫秒)
     * @param isSuccessful 请求是否成功
     * @param code         响应码
     * @param headers      请求头
     * @param contentType  服务器返回数据的数据类型
     * @param bodyString   服务器返回的数据(已解析)
     * @param segments     域名后面的资源地址
     * @param message      响应信息
     * @param responseUrl  请求地址
     */
    fun printJsonResponse(
        chainMs: Long,
        isSuccessful: Boolean,
        code: Int,
        headers: String?,
        contentType: MediaType?,
        bodyString: String?,
        segments: List<String?>?,
        message: String?,
        responseUrl: String?
    )

    /**
     * @param chainMs      服务器响应耗时(单位毫秒)
     * @param isSuccessful 请求是否成功
     * @param code         响应码
     * @param headers      请求头
     * @param segments     域名后面的资源地址
     * @param message      响应信息
     * @param responseUrl  请求地址
     */
    fun printFileResponse(
        chainMs: Long,
        isSuccessful: Boolean,
        code: Int,
        headers: String?,
        segments: List<String?>?,
        message: String?,
        responseUrl: String?
    )
}