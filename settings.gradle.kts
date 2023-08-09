val mod_name: String by extra

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven { url = uri("https://maven.fabricmc.net/") }
        maven { url = uri("https://repo.spongepowered.org/repository/maven-public/") }
        maven { url = uri(System.getenv()["LOCAL_MAVEN"]!!) }
    }
}

rootProject.name = mod_name
include("api", "common", "fabric", "forge")