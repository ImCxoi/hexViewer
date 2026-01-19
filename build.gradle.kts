plugins {
    id("fabric-loom") version "1.11.2"
    id("maven-publish")
    java
}

version = "1.0.0"
group = "hexviewer"
base.archivesName.set("hexviewer")

repositories {
    mavenCentral()
    maven {
        name = "Fabric"
        url = uri("https://maven.fabricmc.net/")
    }
}

dependencies {
    minecraft("com.mojang:minecraft:1.21.10")
    mappings("net.fabricmc:yarn:1.21.10+build.1:v2")

    modImplementation("net.fabricmc:fabric-loader:0.18.4")
    modImplementation("net.fabricmc.fabric-api:fabric-api:0.138.4+1.21.10")
}


tasks.processResources {
    val modVersion = version.toString()
    inputs.property("version", modVersion)
    filesMatching("fabric.mod.json") {
        expand(mapOf("version" to modVersion))
    }
}

tasks.withType<JavaCompile> {
    options.release.set(21)
}

java {
    withSourcesJar()
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}
