pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

    repositories {
        google()
        mavenCentral()
        maven(url = uri("${rootProject.projectDir.absolutePath}/repository"))
    }

    versionCatalogs {
        create("libs") {
            from(files("${rootDir.absolutePath}/libVersions.toml"))
        }

        create("testLibs") {
            from(files("${rootDir.absolutePath}/testLibVersions.toml"))
        }
    }
}

rootProject.name = "AndroidGradleStudy"
include(":app", "modulelib")