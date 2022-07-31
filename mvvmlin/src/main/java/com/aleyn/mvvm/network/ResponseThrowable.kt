package com.aleyn.mvvm.network

import com.aleyn.mvvm.base.IBaseResponse

/**
 *   @author : Aleyn
 *   time   : 2019/08/12
 */
open class ResponseThrowable : Exception {
    var code: Int
    var errMsg: String

    constructor(error: ERROR, e: Throwable? = null) : super(e) {
        code = error.code
        errMsg = error.err
    }

    constructor(code: Int, msg: String, e: Throwable? = null) : super(e) {
        this.code = code
        this.errMsg = msg
    }

    constructor(base: IBaseResponse<*>, e: Throwable? = null) : super(e) {
        this.code = base.code()
        this.errMsg = base.msg()
    }
}

