// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
}

buildscript {
    dependencies {
        // Add the Google Services plugin
        classpath ("com.google.gms:google-services:4.3.15")// Check for the latest version
        classpath ("androidx.navigation:navigation-safe-args-gradle-plugin:2.7.7")
    }
}