
plugins {
    id("dagger.hilt.android.plugin")
    id("android-app-convention")
    id("telegram-reporter")
}


android {
    defaultConfig {
        buildConfigField("String", "API_KEY", "\"${getLocalProperty("API_KEY", project)}\"")
        buildConfigField("String", "YAPASSPORT", "\"${getLocalProperty("YAPASSPORT", project)}\"")
    }
}