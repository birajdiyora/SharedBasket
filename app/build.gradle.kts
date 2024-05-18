plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.devtools.ksp") version "1.8.21-1.0.11"
    id("com.google.dagger.hilt.android")
    id ("com.google.gms.google-services")
}

android {
    namespace = "com.example.sharedbasket"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.sharedbasket"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.7"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("com.google.android.gms:play-services-mlkit-text-recognition-common:19.0.0")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("com.google.android.gms:play-services-location:21.2.0")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.datastore:datastore-core:1.0.0")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("com.google.firebase:firebase-messaging:23.4.1")
//    implementation("com.google.firebase:firebase-firestore:24.11.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    implementation ("androidx.compose.material:material:1.1.1")
    //fireBase
    implementation(platform("com.google.firebase:firebase-bom:30.3.2"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-database-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation ("com.google.firebase:firebase-functions:16.1.3")
//    implementation ("com.google.firebase:firebase-admin:9.2.0")
    //dagger
    implementation("com.google.dagger:hilt-android:2.44.2")
    kapt("com.google.dagger:hilt-android-compiler:2.44.2")
    implementation ("androidx.hilt:hilt-navigation-compose:1.0.0")

    //viewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")
    //navigation
    implementation("androidx.navigation:navigation-compose:2.6.0")
    //maps
    implementation ("com.google.maps.android:android-maps-utils:3.8.0")
    implementation ("com.google.android.gms:play-services-maps:16.1.0")
    implementation ("com.google.maps.android:maps-compose:4.3.3")

    //datastore
    implementation ("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
    implementation ("androidx.datastore:datastore-preferences-rxjava2:1.0.0")
    implementation ("androidx.datastore:datastore-preferences-rxjava3:1.0.0")

    //Gson
    implementation ("com.google.code.gson:gson:2.10.1")

    //navigation
    implementation ("androidx.navigation:navigation-compose:2.6.0")

    //okhttp
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    //firebase auth
    implementation ("androidx.browser:browser:1.5.0")
    implementation ("com.google.android.play:integrity:1.1.0")
    implementation ("com.google.firebase:firebase-appcheck-safetynet:16.1.2")

}
//apply plugin: ("com.google.gms.google-services")