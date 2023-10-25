import com.aleyn.AndroidX
import com.aleyn.BuildConfig
import com.aleyn.Depend
import com.aleyn.Retrofit

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
}

android {
    compileSdk = BuildConfig.compileSdkVersion
    namespace = "com.pcl.mvvm"
    defaultConfig {
        applicationId = BuildConfig.applicationId
        minSdk = BuildConfig.minSdkVersion
        targetSdk = BuildConfig.targetSdkVersion
        versionCode = BuildConfig.versionCode
        versionName = BuildConfig.versionName
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    @Suppress("UnstableApiUsage") buildTypes {
        debug {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }

    @Suppress("UnstableApiUsage") buildFeatures {
        buildConfig = true
        dataBinding = true
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    kotlin {
        jvmToolchain(11)
    }

}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    AndroidX.values.forEach { implementation(it) }
    implementation(AndroidX.recyclerview)
    testImplementation(Depend.junit)
    androidTestImplementation(Depend.androidTestJunit)
    androidTestImplementation(Depend.espressoCore)
    //MVVMLin
    implementation(project("path" to ":mvvmlin"))
    //第三方
    implementation(Depend.banner)
    implementation(Depend.BRVAH)
    implementation(Depend.refreshKernel)
    implementation(Depend.refreshHeader)
    implementation(Depend.bdclta)
    implementation(Depend.bdcltaRv)
    implementation(Depend.netCache)
    Retrofit.values.forEach { implementation(it) }
}
