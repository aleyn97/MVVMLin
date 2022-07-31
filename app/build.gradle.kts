import com.aleyn.BuildConfig
import com.aleyn.Depend
import com.aleyn.AndroidX
import com.aleyn.Retrofit

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
}

android {
    compileSdk = BuildConfig.compileSdkVersion

    defaultConfig {
        applicationId = BuildConfig.applicationId
        minSdk = BuildConfig.minSdkVersion
        targetSdk = BuildConfig.targetSdkVersion
        versionCode = BuildConfig.versionCode
        versionName = BuildConfig.versionName
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    AndroidX.values.forEach { implementation(it) }
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
    implementation(Depend.netCache)
    Retrofit.values.forEach { implementation(it) }
}
