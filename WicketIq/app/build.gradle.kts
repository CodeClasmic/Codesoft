plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)

}

android {
    namespace = "com.projects.wicketiq"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.projects.wicketiq"
        minSdk = 28
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
}

dependencies {
    implementation (libs.appcompat.v161)
    implementation (libs.material)
    implementation (libs.activity.v1101)
    implementation (libs.constraintlayout.v221)
    implementation (libs.gridlayout)
    implementation (platform(libs.firebase.bom.v33100))
    implementation (libs.google.firebase.auth)
    implementation (libs.google.firebase.firestore)
    implementation (libs.google.firebase.database)
    testImplementation (libs.junit)
    androidTestImplementation (libs.junit.v115)
    androidTestImplementation (libs.espresso.core.v351)
    implementation (libs.glide)
    annotationProcessor (libs.compiler)
    implementation (libs.firebase.storage)
    implementation (libs.glide.transformations)

}
