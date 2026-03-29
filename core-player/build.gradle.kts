plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.ksp)

}

android {
    namespace = "com.angel.core.player"
    compileSdk = 34
    defaultConfig {
        minSdk = 24

    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}
kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
    }
}

dependencies {

    implementation(project(":core-model"))

    ksp(libs.hilt.compiler)
    implementation(libs.hilt.android)

    implementation(libs.kotlinx.coroutines.core)
}