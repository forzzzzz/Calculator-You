plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.forz.calculator"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.marktka.calculatorYou"
        minSdk = 27
        targetSdk = 34
        versionCode = 31
        versionName = "3.1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isShrinkResources = true
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        create("benchmark") {
            initWith(buildTypes.getByName("release"))
            signingConfig = signingConfigs.getByName("debug")
            matchingFallbacks += listOf("release")
            isDebuggable = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
    }

    dependenciesInfo {
        // Disables dependency metadata when building APKs.
        includeInApk = false
        // Disables dependency metadata when building Android App Bundles.
        includeInBundle = false
    }

}

dependencies {

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.preference:preference-ktx:1.2.1")
//    testImplementation("junit:junit:4.13.2")
//    androidTestImplementation("androidx.test.ext:junit:1.1.5")
//    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("com.intuit.sdp:sdp-android:1.1.1")
    implementation ("com.mikhaellopez:circleview:1.4.1")
    implementation("androidx.viewpager2:viewpager2:1.1.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.5")
//    implementation("androidx.activity:activity-ktx:1.9.0")

//    debugImplementation ("com.squareup.leakcanary:leakcanary-android:3.0-alpha-1")
}