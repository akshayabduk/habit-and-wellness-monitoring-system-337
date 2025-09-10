androidApplication {
    namespace = "org.example.app"

    dependencies {
        implementation("org.apache.commons:commons-text:1.11.0")
        implementation(project(":utilities"))
        // Core abstractions and models
        implementation(project(":core"))
        // Feature modules
        implementation(project(":profile"))
        implementation(project(":reminders"))
        implementation(project(":bmi"))
        implementation(project(":ai"))
        implementation(project(":notifications"))
    }
}
