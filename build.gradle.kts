// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.google.dagger.hilt.android") version "2.50" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "1.6.10" apply false
    id("com.google.gms.google-services") version "4.4.1" apply false
}
buildscript{
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.1.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.0")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.42")
        classpath("org.jetbrains.kotlin:kotlin-serialization:1.6.10")
        classpath("com.google.gms:google-services:4.4.1")
    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}