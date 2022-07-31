package com.aleyn.mvvm.network

/**
 *   @author : Aleyn
 *   time   : 2019/08/12
 */
enum class ERROR(val code: Int, val err: String) {

    /**
     * 未知错误
     */
    UNKNOWN(1000, "未知错误"),

    /**
     * 解析错误
     */
    PARSE_ERROR(1001, "解析错误"),

    /**
     * 网络错误
     */
    NETWORK_ERROR(1002, "网络错误"),

    /**
     * 协议出错
     */
    HTTP_ERROR(1003, "协议出错"),

    /**
     * 证书出错
     */
    SSL_ERROR(1004, "证书出错"),

    /**
     * 连接超时
     */
    TIMEOUT_ERROR(1006, "连接超时"),

    /**
     * data为空
     */
    DATA_NULL(1007, "返回数据为空");

}