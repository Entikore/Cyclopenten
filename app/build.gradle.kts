import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
    id("dagger.hilt.android.plugin")
    id("org.jlleitschuh.gradle.ktlint") version "11.1.0"
    id("com.google.devtools.ksp").version("1.6.10-1.0.4")
}

android {
    compileSdk = 34

    defaultConfig {
        applicationId = "de.entikore.cyclopenten"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "de.entikore.cyclopenten.CustomTestRunner"

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
        kotlinCompilerExtensionVersion = "1.4.2"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    namespace = "de.entikore.cyclopenten"
}

ktlint {
    android.set(true)
    ignoreFailures.set(false)
    reporters {
        reporter(ReporterType.PLAIN)
        reporter(ReporterType.CHECKSTYLE)
        reporter(ReporterType.SARIF)
    }
}

dependencies {
    val androidTestVersion = "1.5.0"
    val androidTestJunitVersion = "1.1.5"
    val archCoreVersion = "2.2.0"
    val composeVersion = "1.4.3"
    val coroutineVersion = "1.7.1"
    val espressoVersion = "3.5.1"
    val hiltVersion = "2.46.1"
    val lifecycleVersion = "2.6.1"
    val mockitoKotlinVersion = "5.0.0"
    val moshiVersion = "1.15.0"
    val navigationTestVersion = "2.6.0"
    val roomVersion = "2.5.1"

    // App dependencies
    implementation("com.jakewharton.timber:timber:5.0.1")
    implementation("com.squareup.moshi:moshi:$moshiVersion")
    implementation("com.squareup.moshi:moshi-kotlin:$moshiVersion")
    ksp("com.squareup.moshi:moshi-kotlin-codegen:$moshiVersion")
    implementation("com.google.code.gson:gson:2.10.1")

    // Architecture Components
    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.room:room-runtime:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // Hilt
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    kapt("com.google.dagger:hilt-compiler:$hiltVersion")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")

    // Jetpack Compose
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.runtime:runtime-livedata:1.6.0-alpha01")
    implementation("androidx.compose.ui:ui-tooling-preview:$composeVersion")
    implementation("androidx.compose.material:material:$composeVersion")
    implementation("androidx.compose.material:material-icons-extended:$composeVersion")
    implementation("androidx.navigation:navigation-compose:$navigationTestVersion")

    // Dependencies for local unit tests
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.hamcrest:hamcrest-all:1.3")
    testImplementation("androidx.arch.core:core-testing:$archCoreVersion")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutineVersion")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutineVersion")
    testImplementation("org.robolectric:robolectric:4.10.3")
    testImplementation("androidx.navigation:navigation-testing:$navigationTestVersion")
    testImplementation("androidx.test.espresso:espresso-core:$espressoVersion")
    testImplementation("androidx.test.espresso:espresso-contrib:$espressoVersion")
    testImplementation("androidx.test.espresso:espresso-intents:$espressoVersion")
    testImplementation("com.google.truth:truth:1.1.5")
    testImplementation("androidx.compose.ui:ui-test-junit4:$composeVersion")
    testImplementation("org.mockito.kotlin:mockito-kotlin:$mockitoKotlinVersion")

    // JVM tests - Hilt
    testImplementation("com.google.dagger:hilt-android-testing:$hiltVersion")
    kaptTest("com.google.dagger:hilt-compiler:$hiltVersion")

    // AndroidX Test - JVM testing
    testImplementation("androidx.test:core-ktx:$androidTestVersion")
    testImplementation("androidx.test.ext:junit-ktx:$androidTestJunitVersion")
    testImplementation("androidx.test:rules:$androidTestVersion")
    implementation("androidx.test:core:$androidTestVersion")

    // AndroidX Test - Instrumented testing
    androidTestImplementation("androidx.test:core-ktx:$androidTestVersion")
    androidTestImplementation("androidx.test.ext:junit-ktx:$androidTestJunitVersion")
    androidTestImplementation("androidx.test:rules:$androidTestVersion")
    androidTestImplementation("androidx.room:room-testing:$roomVersion")
    androidTestImplementation("androidx.arch.core:core-testing:$archCoreVersion")
    androidTestImplementation("androidx.navigation:navigation-testing:$navigationTestVersion")
    androidTestImplementation("androidx.test.espresso:espresso-core:$espressoVersion")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:$espressoVersion")
    androidTestImplementation("androidx.test.espresso:espresso-intents:$espressoVersion")
    androidTestImplementation("androidx.test.espresso.idling:idling-concurrent:$espressoVersion")
    androidTestImplementation("org.robolectric:annotations:4.10.3")
    implementation("androidx.test.espresso:espresso-idling-resource:$espressoVersion")
    androidTestImplementation("org.mockito:mockito-android:5.4.0")
    androidTestImplementation("org.mockito.kotlin:mockito-kotlin:$mockitoKotlinVersion")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:$composeVersion")
    debugImplementation("androidx.compose.ui:ui-test-manifest:$composeVersion")

    // AndroidX Test - Hilt testing
    androidTestImplementation("com.google.dagger:hilt-android-testing:$hiltVersion")
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:$hiltVersion")
}
