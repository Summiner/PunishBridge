import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("com.github.johnrengelman.shadow") version "8.1.1" apply true
    `java-library`
    `maven-publish`
}

group = "rs.jamie"
version = "1.0.0"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(16))
        sourceCompatibility = JavaVersion.VERSION_16
        targetCompatibility = JavaVersion.VERSION_16
    }
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    maven {
        url = uri("https://jitpack.io")
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.17-R0.1-SNAPSHOT")
    compileOnly("com.gitlab.ruany:LiteBansAPI:0.5.0")
}

tasks {
    processResources {
        inputs.property("version", project.version)
        filesMatching("plugin.yml") {
            expand(getProperties())
            expand(mutableMapOf("version" to project.version))
        }
    }

    named<ShadowJar>("shadowJar") {
        archiveBaseName.set("PunishBridge")
        mergeServiceFiles()
        dependencies {
            exclude("net.kyori")
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])  // Publish the main Java component
            artifact(tasks["shadowJar"]) {
                classifier = "all"  // Add a classifier to the shadow jar
            }
        }
    }

    repositories {
        mavenLocal()  // This tells Gradle to publish to Maven Local
    }
}

tasks.withType<Javadoc>() {
    options.encoding = "UTF-8"
}