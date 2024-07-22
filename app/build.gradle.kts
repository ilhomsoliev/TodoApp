
plugins {
    id("dagger.hilt.android.plugin")
    id("android-app-convention")
    id("telegram-reporter")
}

tgReporter {
    token.set(providers.environmentVariable("TG_TOKEN"))
    chatId.set(providers.environmentVariable("TG_CHAT"))
}

android {
    defaultConfig {
        buildConfigField("String", "API_KEY", "\"${getLocalProperty("API_KEY", project)}\"")
        buildConfigField("String", "YAPASSPORT", "\"${getLocalProperty("YAPASSPORT", project)}\"")
    }
}