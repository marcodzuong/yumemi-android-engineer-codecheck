// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    var kotlin_version: String by extra
    kotlin_version = "1.5.30"
    repositories {
        google()
        mavenCentral()
        jcenter()
    }
    dependencies {
        classpath(BuildPlugins.androidGradlePlugin)
        classpath(BuildPlugins.kotlinGradlePlugin)
        classpath(BuildPlugins.navigationPlugin)
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        jcenter() // Warning: this repository is going to shut down soon
    }
}
//
//task clean(type: Delete) {
//    delete rootProject.buildDir
//}

tasks {
    val clean by registering(Delete::class) {
        delete(buildDir)
    }
}
