plugins {
    alias(libs.plugins.android.library)
//    kotlin("android")
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.angel.feature.player"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

    }

    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }


}

dependencies {
    implementation(project(":core-model"))
    implementation(project(":core-player"))


    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material3)


    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    implementation(libs.kotlinx.coroutines.android)
}