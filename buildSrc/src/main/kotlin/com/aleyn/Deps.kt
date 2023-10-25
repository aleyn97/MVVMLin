package com.aleyn

/**
 *  @Author : Aleyn
 *  @Date  : 2021/2/5 15:28
 */
object Versions {
    const val retrofit = "2.9.0"
    const val appcompat = "1.4.2"
    const val coreKtx = "1.10.1"
    const val material = "1.9.0"
    const val runtimeKtx = "2.6.1"
    const val junit = "4.13.2"
    const val junitExt = "1.1.3"
    const val espressoCore = "3.4.0"

    const val banner = "2.2.2"
    const val BRAVH = "3.0.14"
    const val coil = "2.4.0"
    const val materialDialogs = "3.1.1"
    const val utilCode = "1.30.6"
}

object AndroidX {
    const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    const val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"
    const val material = "com.google.android.material:material:${Versions.material}"
    const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1"
    const val runtimeKtx = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.runtimeKtx}"
    const val recyclerview = "androidx.recyclerview:recyclerview:1.2.1"
    val values = arrayListOf(
        appcompat,
        coreKtx,
        material,
        viewModelKtx,
        runtimeKtx
    )
}

object Retrofit {
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val converterGson = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"
    const val okhttpLogging = "com.squareup.okhttp3:logging-interceptor:4.10.0"
    const val okhttp3 = "com.squareup.okhttp3:okhttp:4.10.0"
    val values = arrayListOf(retrofit, converterGson, okhttpLogging, okhttp3)
}

object Depend {
    const val junit = "junit:junit:${Versions.junit}"
    const val netCache = "com.github.AleynP:net-cache:1.0.0"
    const val androidTestJunit = "androidx.test.ext:junit:${Versions.junitExt}"
    const val espressoCore = "androidx.test.espresso:espresso-core:${Versions.espressoCore}"
    const val banner = "io.github.youth5201314:banner:${Versions.banner}"
    const val BRVAH = "io.github.cymchad:BaseRecyclerViewAdapterHelper:${Versions.BRAVH}"
    const val refreshKernel = "io.github.scwang90:refresh-layout-kernel:2.0.5"
    const val refreshHeader = "io.github.scwang90:refresh-header-classics:2.0.5"
    const val coil = "io.coil-kt:coil:${Versions.coil}" //图片加载
    const val dialogs = "com.afollestad.material-dialogs:lifecycle:${Versions.materialDialogs}"
    const val dialogsCore = "com.afollestad.material-dialogs:core:${Versions.materialDialogs}"
    const val utilCode = "com.blankj:utilcodex:${Versions.utilCode}"

    const val bdclta =
        "me.tatarka.bindingcollectionadapter2:bindingcollectionadapter:4.0.0"
    const val bdcltaRv =
        "me.tatarka.bindingcollectionadapter2:bindingcollectionadapter-recyclerview:4.0.0"

}