package com.aleyn.mvvm.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory

/**
 *   @auther : Aleyn
 *   time   : 2019/11/01
 */
class ViewModelFactory : ViewModelProvider.Factory {

    companion object {
        private var sInstance: ViewModelFactory? = null

        fun getInstance() = sInstance ?: ViewModelFactory().also { sInstance = it }
    }


    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        /* val type = modelClass.constructors[0].parameterTypes
         if (type.isNotEmpty()) {
             val tClass = type[0]
             if (HomeRepository::class.java.isAssignableFrom(tClass)) {
                 return modelClass.getConstructor(tClass).newInstance(Injection.HomeRepository())
             } else if (XXXRepository::class.java.isAssignableFrom(tClass)) {
                 return modelClass.getConstructor(tClass).newInstance(Injection.XXXRepository())
             }
         }*/
        return modelClass.newInstance()
    }
}