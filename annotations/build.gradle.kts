plugins {
    kotlin("jvm")
    `maven-publish`
}

dependencies {
    // https://mvnrepository.com/artifact/org.jetbrains.kotlin/kotlin-reflect
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.7.10")
    // https://mvnrepository.com/artifact/org.javamoney/moneta
    implementation("org.javamoney:moneta:1.4.2")
    // https://mvnrepository.com/artifact/javax.money/money-api
    implementation("javax.money:money-api:1.1")


}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "$groupId"
            artifactId = "fixture-factory-annotations"
            version = "0.1.0"
            from(components["java"])
        }
    }
}
