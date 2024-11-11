plugins {
    id ("com.android.application")
    id ("org.jetbrains.kotlin.android")
    id ("kotlin-kapt")
    id ("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "com.staffrakho"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.staffrakho"
        minSdk = 24
        targetSdk = 34
        versionCode = 2
        versionName = "2"
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }


    buildFeatures {
        viewBinding = true
        dataBinding =  true
    }
    buildToolsVersion = "34.0.0"

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.inappmessaging.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)



    // retrofit
    implementation (libs.retrofit)
    implementation (libs.converter.gson)
    implementation (libs.gson)
    implementation(libs.logging.interceptor)

    //Image picker
    implementation(libs.imagepicker)

    //glide
    implementation(libs.glide)
    annotationProcessor(libs.compiler)


    // Fragment NavController
    implementation(libs.androidx.navigation.fragment.ktx)


    //OTP View
    implementation (libs.otpview)

    //Swipe Refresh Layout
    implementation (libs.androidx.swiperefreshlayout)


    implementation(libs.ssp.android)
    implementation(libs.sdp.android)
    implementation(kotlin("script-runtime"))

    // Import the BoM for the Firebase platform
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)
    implementation(libs.play.services.auth)
    implementation (libs.firebase.messaging.ktx)

    // Google Add
    implementation (libs.play.services.ads)

//    implementation (libs.android.pdf.viewer)


}