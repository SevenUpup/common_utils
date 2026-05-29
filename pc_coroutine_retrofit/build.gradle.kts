plugins {
//    id("java-library")
//    alias(libs.plugins.org.jetbrains.kotlin.jvm)
//    id("kotlin")
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.fido.pc_coroutine_retrofit"
    compileSdk = 32
    defaultConfig {
        minSdk = 24
        targetSdk = 32
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}
//kotlin {
//    compilerOptions {
//        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
//    }
//}
dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.7.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation("com.squareup.okio:okio:2.10.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.google.protobuf:protobuf-java:3.5.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")
}
