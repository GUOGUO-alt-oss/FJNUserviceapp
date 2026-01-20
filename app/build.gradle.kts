plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.fjnuserviceapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.fjnuserviceapp"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        // 高德SDK API Key（需自己去高德开发者平台申请）
        manifestPlaceholders["AMAP_KEY"] = "你的高德API Key"
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
    // 启用ViewBinding
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // MVVM架构
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.6.2")
    implementation("androidx.lifecycle:lifecycle-livedata:2.6.2")
    implementation("androidx.lifecycle:lifecycle-runtime:2.6.2")

    // Room数据库
    implementation("androidx.room:room-runtime:2.5.2")
    annotationProcessor("androidx.room:room-compiler:2.5.2")

    // 网络请求
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.google.code.gson:gson:2.10.1")

    // 图片加载
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")

    // 新增：通知模块必需的2个依赖（ViewPager2 + RecyclerView）
    implementation("androidx.viewpager2:viewpager2:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // 3D Sceneform (Maintained version)
    implementation("com.gorisse.thomas.sceneform:sceneform:1.23.0")
    
    // 高德地图SDK
    implementation("com.amap.api:3dmap:9.7.0")
    implementation("com.amap.api:search:9.7.0")

    // Security
    implementation("androidx.security:security-crypto:1.0.0")
}