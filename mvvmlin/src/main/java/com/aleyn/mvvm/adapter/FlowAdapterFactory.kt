package com.aleyn.mvvm.adapter

import kotlinx.coroutines.flow.Flow
import retrofit2.CallAdapter
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * @author : Aleyn
 * @date : 2022/07/24 : 14:46
 */
class FlowAdapterFactory private constructor(private val async: Boolean) :
    CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit,
    ): CallAdapter<*, *>? {
        val rawType = getRawType(returnType)
        if (rawType != Flow::class.java) return null

        require(returnType is ParameterizedType) { "the flow type  is error" }

        val flowType = getParameterUpperBound(0, returnType)
        val rawFlowType = getRawType(flowType)

        return if (rawFlowType == Response::class.java) {
            require(flowType is ParameterizedType) {
                "Response must be parameterized as Response<Foo> or Response<? extends Foo>"
            }
            val responseBodyType = getParameterUpperBound(0, flowType)
            FlowCallAdapter(responseBodyType, false, async)
        } else {
            FlowCallAdapter(flowType, true, async)
        }

    }

    companion object {
        @JvmStatic
        fun create(async: Boolean = false) = FlowAdapterFactory(async)
    }
}