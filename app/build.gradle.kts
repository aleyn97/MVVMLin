plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    compileSdk = libs.versions.compileSdk.get().toInt()
    namespace = "com.pcl.mvvm"
    defaultConfig {
        applicationId = libs.versions.applicationId.get()
        minSdk = libs.versions.minSdkVersion.get().toInt()
        targetSdk = libs.versions.minSdkVersion.get().toInt()
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.versionName.get()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
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

    buildFeatures {
        buildConfig = true
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
    implementation(libs.bundles.androidX)
    implementation(libs.material)
    implementation(libs.recyclerview)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidTestJunit)
    androidTestImplementation(libs.espressoCore)
    //MVVMLin
    implementation(project("path" to ":mvvmlin"))
    //第三方
    implementation(libs.banner)
    implementation(libs.bravh)
    implementation(libs.refreshKernel)
    implementation(libs.refreshHeader)
    implementation(libs.netCache)
    implementation(libs.bundles.netWork)
    implementation(libs.coil)
}
