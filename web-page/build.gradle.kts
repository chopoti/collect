import dependencies.Dependencies
import dependencies.Versions

plugins {
    id("com.android.library")
    id("kotlin-android")
}

apply(from = "../config/quality.gradle")

android {
    namespace = "org.fsr.collect.webpage"

    compileSdk = Versions.android_compile_sdk

    defaultConfig {
        minSdk = Versions.android_min_sdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    coreLibraryDesugaring(Dependencies.desugar)

    implementation(project(":androidshared"))
    implementation(project(":icons"))
    implementation(project(":strings"))
    implementation(Dependencies.android_material)
    implementation(Dependencies.androidx_browser)

    testImplementation(Dependencies.androidx_test_ext_junit)
    testImplementation(Dependencies.hamcrest)
    testImplementation(Dependencies.robolectric)
}
