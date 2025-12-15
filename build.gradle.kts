plugins {
    `java-library`
    id("com.gradleup.shadow") version "9.3.0"
}

group = "net.meano"
version = "1.4"

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
        name = "devmart-other"
        url = uri("https://nexuslite.gcnt.net/repos/other/")
    }
}

val adventureVersion = "4.25.0"

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.21.11-R0.1-SNAPSHOT")

    compileOnly("org.apache.logging.log4j:log4j-api:2.25.2")
    implementation("org.bstats:bstats-bukkit:3.1.0")
    api("com.tcoded:FoliaLib:0.4.2")

    api("net.kyori:adventure-platform-bukkit:4.4.1")
    api("net.kyori:adventure-api:$adventureVersion")
    api("net.kyori:adventure-text-serializer-legacy:$adventureVersion")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

configure<JavaPluginExtension> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.build.configure {
    dependsOn("shadowJar")
}

tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    archiveFileName = "${project.name}-${project.version}.${archiveExtension.get()}"
    exclude("META-INF/**") // Dreeam - Avoid to include META-INF/maven in Jar
    minimize {
        exclude(dependency("com.tcoded.folialib:.*:.*"))
    }
    relocate("net.kyori", "net.meano.libs.kyori")
    relocate("org.bstats", "net.meano.libs.bstats")
    relocate("com.tcoded.folialib", "net.meano.libs.folialib")
}

tasks {
    processResources {
        filesMatching("**/plugin.yml") {
            expand("version" to project.version)
        }
    }
}
