plugins {
    kotlin("jvm")
    `maven-publish`
}

// Versions are declared in 'gradle.properties' file
val kspVersion: String by project

dependencies {
    implementation(project(":annotations"))
    implementation("com.google.devtools.ksp:symbol-processing-api:$kspVersion")
    // https://mvnrepository.com/artifact/com.squareup/kotlinpoet
    implementation( group = "com.squareup", name = "kotlinpoet", version = "1.12.0")

}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "$groupId"
            artifactId = "fixture-factory-processor"
            version = "0.1.0"
            from(components["java"])
        }
    }
}
