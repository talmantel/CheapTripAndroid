import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("dev.icerock.mobile.multiplatform-resources")
    id("com.squareup.sqldelight")
}

kotlin {
    android()
    ios {
        binaries {
            framework {
                baseName = "shared"
            }
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")
                api("dev.icerock.moko:resources:0.15.1")
                api("dev.icerock.moko:mvvm-core:0.10.1")
                api("dev.icerock.moko:mvvm-livedata:0.10.1")
                implementation("com.squareup.sqldelight:runtime:1.4.4")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.2")
                implementation("com.google.android.material:material:1.3.0")
                implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1")
                api("dev.icerock.moko:mvvm-livedata-material:0.10.1")
                api("dev.icerock.moko:mvvm-databinding:0.10.1")
                api("dev.icerock.moko:mvvm-viewbinding:0.10.1")
                implementation("com.squareup.sqldelight:android-driver:1.4.4")
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("junit:junit:4.13.2")
                implementation("androidx.test:core:1.3.0")
                implementation("com.android.support:support-annotations:28.0.0")
                implementation("com.android.support.test:runner:1.0.2")
                implementation("org.robolectric:robolectric:4.4")
            }
        }
        val iosMain by getting {
            dependencies {
                implementation("com.squareup.sqldelight:native-driver:1.4.4")
            }
        }
        val iosTest by getting
    }
    // export correct artifact to use all classes of library directly from Swift
    targets.withType(KotlinNativeTarget::class.java).all {
        val arch = when (this.konanTarget) {
            org.jetbrains.kotlin.konan.target.KonanTarget.IOS_ARM64 -> "iosarm64"
            org.jetbrains.kotlin.konan.target.KonanTarget.IOS_X64 -> "iosx64"
            else -> throw IllegalArgumentException()
        }
        binaries.withType(org.jetbrains.kotlin.gradle.plugin.mpp.Framework::class.java).all {
            export("dev.icerock.moko:mvvm-$arch:0.10.1")
        }
    }
}

android {
    compileSdkVersion(30)
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdkVersion(24)
        targetSdkVersion(30)
    }
    testOptions {
        unitTests {
            isReturnDefaultValues = true
            isIncludeAndroidResources = true
        }
    }
}

multiplatformResources {
    multiplatformResourcesPackage = "ru.z8.louttsev.cheaptripmobile"
}

sqldelight {
    database("LocalDb") {
        packageName = "ru.z8.louttsev.cheaptripmobile.shared.infrastructure.persistence"
        sourceFolders = listOf("sqldelightLocalDb")
    }
    database("FullDb") {
        packageName = "ru.z8.louttsev.cheaptripmobile.shared.infrastructure.datasource"
        sourceFolders = listOf("sqldelightFullDb")
    }
}

val packForXcode by tasks.creating(Sync::class) {
    group = "build"
    val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
    val sdkName = System.getenv("SDK_NAME") ?: "iphonesimulator"
    val targetName = "ios" + if (sdkName.startsWith("iphoneos")) "Arm64" else "X64"
    val framework =
        kotlin.targets.getByName<KotlinNativeTarget>(targetName).binaries.getFramework(mode)
    inputs.property("mode", mode)
    dependsOn(framework.linkTask)
    val targetDir = File(buildDir, "xcode-frameworks")
    from({ framework.outputDirectory })
    into(targetDir)
}

tasks.getByName("build").dependsOn(packForXcode)