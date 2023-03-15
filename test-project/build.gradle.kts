plugins {
    kotlin("jvm")
    id("com.google.devtools.ksp")
}


dependencies {
    implementation(project(":annotations"))

    // https://mvnrepository.com/artifact/org.javamoney/moneta
    implementation("org.javamoney:moneta:1.4.2")
    // https://mvnrepository.com/artifact/javax.money/money-api
    implementation("javax.money:money-api:1.1")

    kspTest(project(":processor"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.4")
    testImplementation(platform("org.junit:junit-bom:5.7.0"))

}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
