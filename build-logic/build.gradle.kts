plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

gradlePlugin {
    plugins.register("telegram-reporter") {
        id = "telegram-reporter"
        implementationClass = "TelegramReporterPlugin"
    }
}

dependencies {
    implementation(libs.agp)
    implementation(libs.kotlin.gradle.plugin)
    implementation(libs.kotlin.kapt.gradle.plugin)
    implementation(libs.compose.gradle.plugin)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}