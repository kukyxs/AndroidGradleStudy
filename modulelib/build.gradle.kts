plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin)
    id("maven-publish")
}

android {
    compileSdk = libs.versions.compileSdk.get().toInt()
    buildToolsVersion = libs.versions.buildToolsVersion.get()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }

        debug {
            isMinifyEnabled = false
        }

        create("custom") {
            initWith(getByName("debug"))
        }
    }

    flavorDimensions.addAll(arrayListOf("api", "abm"))

//    productFlavors {
//        create("free1") { dimension = "api" }
//        create("pro1") { dimension = "api" }
//        create("free") { dimension = "abm" }
//        create("pro") { dimension = "abm" }
//    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    testImplementation(testLibs.junit)
    androidTestImplementation(testLibs.bundles.test.base)
    implementation(libs.bundles.android.base)
}

task("generateSourcesJar", type = Jar::class) {
    from(android.sourceSets.getByName("main").java.srcDirs)
    archiveClassifier.set("sources")
}

afterEvaluate {
    publishing {
        repositories { maven(url = uri("${rootProject.projectDir.absolutePath}/repository")) }
        publications {
            create("releaseLib", type = MavenPublication::class) {
                from(components.getByName("release"))
                groupId = "com.kuky.library"
                artifactId = "mylibrary"
                version = "0.0.1"
            }
        }
    }
}