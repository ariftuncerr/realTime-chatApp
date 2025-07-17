import org.jetbrains.kotlin.fir.declarations.builder.buildScript

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
    alias(libs.plugins.google.gms.google.services)

}
buildscript {
    dependencies {
        classpath ("com.android.tools.build:gradle:8.3.2")
        classpath ("com.google.gms:google-services:4.4.3")
    }
}

android {
    namespace = "com.ariftuncer.realtime_chatapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.ariftuncer.realtime_chatapp"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }


    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.play.services.ads)
    implementation(libs.firebase.auth)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


    // ViewModel, LiveData
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.9.1")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.9.1")

    // Navigation Component
    implementation ("androidx.navigation:navigation-fragment-ktx:2.9.1")
    implementation( "androidx.navigation:navigation-ui-ktx:2.7.7")

    // Hilt (dependency injection)
    implementation ("com.google.dagger:hilt-android:2.51")
    kapt ("com.google.dagger:hilt-compiler:2.51")
    implementation ("androidx.hilt:hilt-navigation-fragment:1.2.0")

    // Coroutines
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")


    //Firebase
    implementation (platform("com.google.firebase:firebase-bom:33.16.0"))
    implementation ("com.google.firebase:firebase-auth-ktx")
    implementation ("com.google.android.gms:play-services-auth:21.3.0")
    implementation("com.google.firebase:firebase-firestore-ktx:25.1.4")



}