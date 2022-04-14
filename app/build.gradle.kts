@file:Suppress("LABEL_NAME_CLASH")

import com.android.build.gradle.internal.api.ApkVariantOutputImpl
import com.android.build.gradle.internal.api.ApplicationVariantImpl
import java.text.SimpleDateFormat
import java.util.*

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin)
}

android {
    compileSdk = libs.versions.compileSdk.get().toInt()
    buildToolsVersion = libs.versions.buildToolsVersion.get()

    defaultConfig {
        applicationId = "com.kuky.androidgradlestudy"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.versionName.get()
        multiDexEnabled = true
        multiDexKeepProguard = file("multidex-keep-rules.pro")

        ndk {
            abiFilters.addAll(arrayListOf("armeabi-v7a", "arm64-v8a"))
        }

        vectorDrawables {
//            generatedDensities("mhdpi", "xhdpi")
            useSupportLibrary = true
        }

        resourceConfigurations.addAll(arrayListOf("zh", "en"))
        buildConfigField("String", "name", "\"Test\"")
        resValue("string", "age", "12 years")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isShrinkResources = true
            isMinifyEnabled = true
            manifestPlaceholders["icon"] = "@mipmap/ic_launcher"
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            buildConfigField("String", "type", "\"Release\"")
        }

        debug {
            isDebuggable = true
            isJniDebuggable = true
            isRenderscriptDebuggable = true

            applicationIdSuffix = ".debug"
            manifestPlaceholders["icon"] = "@mipmap/ava"
            buildConfigField("String", "type", "\"Debug\"")
        }

        create("custom") {
            initWith(getByName("debug"))
            manifestPlaceholders["icon"] = "@mipmap/ic_launcher"
            applicationIdSuffix = ".debugcustom"
            buildConfigField("String", "type", "\"Custom\"")
        }
    }

    flavorDimensions.addAll(arrayListOf("api", "abm"))
    productFlavors {
        create("demo") {
            applicationId = "com.kuky.abm"
            dimension = "abm"
            applicationIdSuffix = ".demo"
            matchingFallbacks.addAll(arrayListOf("free", "pro"))
        }

        create("v2") {
            dimension = "abm"
            applicationIdSuffix = ".v2"
            matchingFallbacks.addAll(arrayListOf("pro", "free"))
        }

        create("minV21") {
            dimension = "api"
            applicationIdSuffix = ".min21"
            versionNameSuffix = "-min21v"
            minSdk = 21
            matchingFallbacks.addAll(arrayListOf("free1", "pro1"))
        }

        create("minV30") {
            applicationId = "com.kuky.api"
            dimension = "api"
            applicationIdSuffix = ".min30"
            minSdk = 30
            matchingFallbacks.addAll(arrayListOf("pro1", "free1"))
        }
    }

    variantFilter {
        val names = flavors.map { it.name }
        ignore = ((names.contains("minV30")) && names.contains("v2")) ||
                (names.contains("minV21") && names.contains("demo"))
    }

    sourceSets {
        getByName("demo") {
            java.srcDirs("src/main/java", "src/main/filters")
        }
    }

    lint { abortOnError = false }

//    splits {
//        abi {
//            isEnable = true
//            exclude("arm64-v8a")
//        }
//
//        density {
//            isEnable = true
//            compatibleScreens("small", "large", "normal", "xlarge")
//        }
//    }

    externalNativeBuild {
        cmake {
            path(file("src/main/cpp/CMakeLists.txt"))
            version = "3.18.1"
        }
    }

    androidResources {
        additionalParameters("--rename-manifest-package", "com.test.gradle")
    }

    dataBinding { isEnabled = true }

    kotlinOptions { jvmTarget = "1.8" }

    val date = SimpleDateFormat("yyyyMMddHHmm").format(Date())

    // Kts Style
    applicationVariants.all {
        val variant = this as? ApplicationVariantImpl ?: return@all
        outputs.all {
            val output = this as? ApkVariantOutputImpl ?: return@all
            output.versionNameOverride = "v${date}"
            output.versionCodeOverride = (date.toLong() / 10000).toInt()
            output.outputFileName = "${output.versionNameOverride}-${variant.buildType.name}-${date}.apk"
        }
    }

// Groovy Style
//
//    def date = new Date().format("yyyyMMddHHmm")
//    applicationVariants.all { /*ApplicationVariantImpl*/ variant ->
//        println "${variant.applicationId}, ${variant.name}，${variant.description}, ${variant.versionName}"
//
//        variant.outputs.all { /*ApkVariantOutputImpl*/ output ->
//            output.versionNameOverride = "v${date}"
//            output.versionCodeOverride = Long.parseLong(date) / 10000
//            output.outputFileName = "${output.versionNameOverride}-${variant.buildType.name}-${date}.apk"
//            println output.outputFileName
//        }
//    }
//
}

dependencies {
//    implementation "com.android.tools.build:gradle:7.1.2"

    testImplementation(testLibs.junit)
    androidTestImplementation(testLibs.bundles.test.base)

    implementation(libs.bundles.android.base)
    implementation(libs.test.lib)
}