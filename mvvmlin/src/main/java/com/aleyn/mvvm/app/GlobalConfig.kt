package com.aleyn.mvvm.app

import androidx.lifecycle.ViewModelProvider
import com.aleyn.mvvm.base.ViewModelFactory

/**
 *   @auther : Aleyn
 *   time   : 2019/11/12
 */
class GlobalConfig {
    var viewModelFactory: ViewModelProvider.NewInstanceFactory = ViewModelFactory()
}