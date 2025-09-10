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

        // UI dependencies for traditional Views and Material components
        implementation("androidx.appcompat:appcompat:1.7.0")
        implementation("com.google.android.material:material:1.12.0")
        implementation("androidx.fragment:fragment-ktx:1.8.3")
    }
}
