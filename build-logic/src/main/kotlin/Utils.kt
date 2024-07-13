import com.android.build.gradle.BaseExtension
import org.gradle.api.Project
import java.io.FileInputStream
import java.util.Properties


fun getLocalProperty(propertyName: String, project: Project): String? {
    val propertiesFile = project.rootProject.file("local.properties")
    if (propertiesFile.exists()) {
        val properties = Properties()
        properties.load(FileInputStream(propertiesFile))
        return properties.getProperty(propertyName)
    }
    return null
}

fun BaseExtension.baseAndroidConfig() {
    namespace = AndroidConst.NAMESPACE
    setCompileSdkVersion(AndroidConst.COMPILE_SKD)
    defaultConfig {
        minSdk = AndroidConst.MIN_SKD
        vectorDrawables {
            useSupportLibrary = true
        }
//        buildConfigField("String", "API_KEY", "\"${getLocalProperty("API_KEY", project)}\"")
//        buildConfigField("String", "YAPASSPORT", "\"${getLocalProperty("YAPASSPORT", project)}\"")
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = AndroidConst.COMPILE_JDK_VERSION
        targetCompatibility = AndroidConst.COMPILE_JDK_VERSION
    }
    kotlinOptions {
        jvmTarget = AndroidConst.KOTLIN_JVM_TARGET
    }
}