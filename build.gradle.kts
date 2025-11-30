plugins {
    kotlin("jvm") version "2.2.21"
    application
}

sourceSets {
    main {
        kotlin.srcDir("src")
    }
}

application {
    mainClass = providers.gradleProperty("day").map { "Day${it}Kt" }
}

tasks {
    wrapper {
        gradleVersion = "9.2.1"
    }
}
