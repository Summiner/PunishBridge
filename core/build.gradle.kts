configurations.named("shadow") {
    extendsFrom(configurations.getByName("implementation"))
}

tasks {
    shadowJar {
        archiveFileName.set("punishbridge-core.jar")
    }
}