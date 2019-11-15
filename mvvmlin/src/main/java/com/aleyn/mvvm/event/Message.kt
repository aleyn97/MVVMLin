package com.aleyn.mvvm.event

/**
 *   @auther : Aleyn
 *   time   : 2019/11/13
 */
class Message {
    var code: Int
    var msg: String
    var arg1: Int
    var arg2: Int
    var obj: Any?

    constructor(code: Int = 0, msg: String = "", arg1: Int = 0, arg2: Int = 0, obj: Any? = null) {
        this.code = code
        this.msg = msg
        this.arg1 = arg1
        this.arg2 = arg2
        this.obj = obj
    }
}