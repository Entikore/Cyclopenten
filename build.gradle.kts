plugins {
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
    id("com.android.application") version "8.0.0" apply false
    id("com.android.library") version "8.0.0" apply false
    id("com.google.dagger.hilt.android") version "2.45" apply false
}

tasks.register("clean", Delete::class){
    delete(rootProject.buildDir)
}