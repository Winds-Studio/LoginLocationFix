plugins {
    `java-library`
}

group = "net.meano"
version = "0.12"

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

val adventureVersion = "4.14.0"

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.20.3-R0.1-SNAPSHOT")

    api("com.tcoded:FoliaLib:0.3.1")
    api("net.kyori:adventure-platform-bukkit:4.3.1")
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

tasks {
    processResources {
        expand("version" to project.version)
    }
}
