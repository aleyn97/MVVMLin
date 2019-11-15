package com.aleyn.mvvm.network

/**
 *   @auther : Aleyn
 *   time   : 2019/08/12
 */
class ResponseThrowable : Exception {
    var code: Int
    var errMsg: String

    constructor(error: ERROR) {
        code = error.getKey()
        errMsg = error.getValue()
    }

    constructor(code: Int, msg: String) {
        this.code = code
        this.errMsg = msg
    }
}

