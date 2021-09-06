plugins {
    id("dev.fritz2.fritz2-gradle") version "0.12"
}

repositories {
    mavenLocal()
    mavenCentral()
}

kotlin {
    jvm()
    js(IR) {
        browser {
            commonWebpackConfig {
                cssSupport.enabled = true
            }
        }
    }.binaries.executable()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("dev.fritz2:core:0.13-SNAPSHOT")
                // see https://components.fritz2.dev/
                // implementation("dev.fritz2:components:0.11.1")
            }
        }
        val jvmMain by getting {
            dependencies {
            }
        }
        val jsMain by getting {
            dependencies {
                // tailwind
                implementation(npm("postcss", "8.3.5"))
                implementation(npm("postcss-loader", "4.2.0")) // 5.0.0 seems not to work
                implementation(npm("autoprefixer", "10.2.6"))
                implementation(npm("tailwindcss", "2.2.4"))
                implementation(npm("@tailwindcss/forms", "0.3.3"))
            }
        }
    }
}

val copyTailwindConfig = tasks.register<Copy>("copyTailwindConfig") {
    from(".")
    into("${rootProject.buildDir}/js/packages/${rootProject.name}") //-${project.name}")
    include("tailwind.config.js")
}

val copyPostcssConfig = tasks.register<Copy>("copyPostcssConfig") {
    from(".")
    into("${rootProject.buildDir}/js/packages/${rootProject.name}") //-${project.name}"
    include("postcss.config.js")
}

tasks.named("jsBrowserProductionWebpack") {
    dependsOn(copyTailwindConfig)
    dependsOn(copyPostcssConfig)
}

tasks.named("jsRun") {
    dependsOn(copyTailwindConfig)
    dependsOn(copyPostcssConfig)
}
