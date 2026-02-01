plugins {
    `java-library`
    id("com.gradleup.shadow") version "9.3.1"
}

group = "net.meano"
version = "1.5"
description = "Fix player location when they login, prevent stuck in blocks"

repositories {
    mavenCentral()

    // SpigotMC
    maven {
        name = "spigotmc-repo"
        url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }

    // PaperMC
    maven {
        name = "papermc-repo"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }

    // FoliaLib
    maven {
        name = "tcoded-releases"
        url = uri("https://repo.tcoded.com/releases")
    }
}

val adventureVersion = "4.26.1"

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.21.11-R0.2-SNAPSHOT")

    compileOnly("org.apache.logging.log4j:log4j-api:2.25.3")
    implementation("org.bstats:bstats-bukkit:3.1.0")
    api("com.tcoded:FoliaLib:0.5.1")

    api("net.kyori:adventure-platform-bukkit:4.4.1")
    api("net.kyori:adventure-api:$adventureVersion")
    api("net.kyori:adventure-text-serializer-legacy:$adventureVersion")
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    build {
        dependsOn(shadowJar)
    }

    shadowJar {
        archiveFileName.set("${project.name}-${project.version}.${archiveExtension.get()}")
        exclude("META-INF/**") // Dreeam - Avoid to include META-INF/maven in Jar
        minimize {
            exclude(dependency("com.tcoded.folialib:.*:.*"))
        }
        relocate("net.kyori", "${project.group}.libs.kyori")
        relocate("org.bstats", "${project.group}.libs.bstats")
        relocate("com.tcoded.folialib", "${project.group}.libs.folialib")
    }

    processResources {
        filesMatching("**/plugin.yml") {
            expand(
                mapOf(
                    "version" to project.version,
                    "description" to description
                )
            )
        }
    }
}
