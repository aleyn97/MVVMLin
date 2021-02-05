package com.aleyn

/**
 *  @Author : Aleyn
 *  @Date  : 2021/2/5 15:28
 */
object Versions {
    const val retrofit = "2.9.0"
    const val appcompat = "1.2.0"
    const val coreKtx = "1.3.2"
    const val material = "1.2.1"
    const val constraintlayout = "2.0.4"
    const val buildGradle = "3.6.3"
    const val kotlin = "1.4.21"
    const val bintrayRelease = "0.9.1"
    const val extensions = "2.2.0"
    const val room = "2.3.0-alpha01"
    const val lifecycle = "2.2.0"

    const val junit = "4.12"
    const val junitExt = "1.1.2"
    const val espressoCore = "3.3.0"

    const val banner = "2.1.0"
    const val BRVAH = "3.0.4"
    const val immersionbar = "3.0.0"
    const val coil = "1.1.1"
    const val materialDialogs = "3.1.1"
    const val utilCode = "1.30.5"
    const val bottomTab = "2.3.0X"
    const val bdclta = "3.1.1"
    const val swiperefresh = "1.1.0"
}

object AndroidX {
    const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    const val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"
    const val constraintlayout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraintlayout}"
    const val material = "com.google.android.material:material:${Versions.material}"
    const val extensions = "androidx.lifecycle:lifecycle-extensions:${Versions.extensions}"
    const val lifecycleViewmodelKtx =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"

    val values = arrayListOf(
        appcompat,
        coreKtx,
        constraintlayout,
        material,
        extensions,
        lifecycleViewmodelKtx
    )
}

object Kt {
    const val stdlibJdk7 = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
    const val stdlibJdk8 = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}"
    const val test = "org.jetbrains.kotlin:kotlin-test-junit:${Versions.kotlin}"
    const val plugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
}

object Room {
    const val runtime = "androidx.room:room-runtime:${Versions.room}"
    const val compiler = "androidx.room:room-compiler:${Versions.room}"
    const val ktx = "androidx.room:room-ktx:${Versions.room}"
    const val testing = "androidx.room:room-testing:${Versions.room}"
}

object Retrofit {
    const val runtime = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val gson = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"
    val values = arrayListOf(runtime, gson)
}

object Depend {
    const val junit = "junit:junit:${Versions.junit}"
    const val androidTestJunit = "androidx.test.ext:junit:${Versions.junitExt}"
    const val espressoCore = "androidx.test.espresso:espresso-core:${Versions.espressoCore}"
    const val banner = "com.youth.banner:banner:${Versions.banner}"
    const val BRVAH = "com.github.CymChad:BaseRecyclerViewAdapterHelper:${Versions.BRVAH}"
    const val immersionbar = "com.gyf.immersionbar:immersionbar:${Versions.immersionbar}"
    const val coil = "io.coil-kt:coil:${Versions.coil}" //图片加载
    const val dialogs = "com.afollestad.material-dialogs:lifecycle:${Versions.materialDialogs}"
    const val dialogsCore = "com.afollestad.material-dialogs:core:${Versions.materialDialogs}"
    const val utilCode = "com.blankj:utilcodex:${Versions.utilCode}"
    const val bottomTab = "me.majiajie:pager-bottom-tab-strip:${Versions.bottomTab}"
    const val bdclta =
        "me.tatarka.bindingcollectionadapter2:bindingcollectionadapter:${Versions.bdclta}"
    const val bdcltaRv =
        "me.tatarka.bindingcollectionadapter2:bindingcollectionadapter-recyclerview:${Versions.bdclta}"
    const val swiperefresh =
        "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.swiperefresh}"
}