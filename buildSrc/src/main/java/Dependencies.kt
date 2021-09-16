const val kotlinVersion = "1.5.30"
const val navigation = "2.3.5"
object Releases {
    val versionCode = 1
    val versionName = "1.0"
}

object AndroidSdk {
    const val min = 23
    const val compile = 31
    const val target = compile
    const val buildToolVersion = "29.0.1"
}

object AppConfig {
    const val applicationId = "jp.co.yumemi.android.codecheck"
    const val versionCode = 1
    const val versionName = "1.0"
    const val testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
}

object Libraries {
    private object Versions {
        const val constraintLayout = "2.1.0"
        const val appCompat = "1.3.1"
        const val ktx = "1.6.0"
        const val material = "1.4.0"
        const val recyclerView = "1.2.1"
        const val koin = "2.2.0-rc-3"
        const val coil = "1.3.2"
        const val ktor = "1.6.3"
        const val lifecycle = "2.3.1"

        const val coroutines = "1.5.1"
        const val mockito = "2.23.4"
    }

    const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion"
    const val appCompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
    const val ktxCore = "androidx.core:core-ktx:${Versions.ktx}"
    const val constraintLayout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    const val materialDesign = "com.google.android.material:material:${Versions.material}"
    const val recyclerView = "androidx.recyclerview:recyclerview:${Versions.recyclerView}"

    const val koin = "org.koin:koin-android:${Versions.koin}"
    const val koinScope = "org.koin:koin-androidx-scope:${Versions.koin}"
    const val koinViewModel = "org.koin:koin-androidx-viewmodel:${Versions.koin}"
    const val coil = "io.coil-kt:coil:${Versions.coil}"
    const val ktor = "io.ktor:ktor-client-android:${Versions.ktor}"
    const val lifecycleViewModel =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
    const val lifecycleLivedata = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycle}"
    const val lifecycleRuntime = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}"
    const val navigationFragment =
        "androidx.navigation:navigation-fragment-ktx:${navigation}"
    const val navigationUi = "androidx.navigation:navigation-ui-ktx:${navigation}"
    const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
    const val mockitoCore = "org.mockito:mockito-core:${Versions.mockito}"
    const val mockitoAndroid = "org.mockito:mockito-android:${Versions.mockito}"
}

object TestLibraries {
    private object Versions {
        const val junit = "4.13.2"
        const val junitExt = "1.1.3"
        const val espresso = "3.4.0"
    }

    const val junit = "junit:junit:${Versions.junit}"
    const val junitExt = "androidx.test.ext:junit:${Versions.junitExt}"
    const val espresso = "androidx.test.espresso:espresso-core:${Versions.espresso}"
}

object BuildPlugins {
    object Versions {
        const val buildToolVersion = "7.0.1"
    }

    const val androidGradlePlugin = "com.android.tools.build:gradle:${Versions.buildToolVersion}"
    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"
    const val navigationPlugin = "androidx.navigation:navigation-safe-args-gradle-plugin:${navigation}"
    const val androidApplication = "com.android.application"
    const val kotlinAndroid = "kotlin-android"
    const val kotlinAndroidExtensions = "kotlin-android-extensions"
    const val javaLib = "java-library"
    const val androidLib = "com.android.library"

    object BuildType {
        const val release = "release"
        const val debug = "debug"
    }
}