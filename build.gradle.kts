import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.1.5"
    id("io.spring.dependency-management") version "1.1.3"
    kotlin("jvm") version "1.9.20"
    kotlin("plugin.spring") version "1.9.20"
    kotlin("plugin.jpa") version "1.9.20"
    kotlin("plugin.allopen") version "1.9.20"
}
allOpen {
    annotation("jakarta.persistence.Entity")
}

group = "com.kotlin"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework.boot:spring-boot-starter-web:3.1.5")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    // spring security
    implementation("org.springframework.boot:spring-boot-starter-security:3.1.5")
    testImplementation("org.springframework.security:spring-security-test")
    // jwt
    implementation("io.jsonwebtoken:jjwt-api:0.12.3")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.3")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.3")
    // mysql
    implementation("com.mysql:mysql-connector-j:8.1.0")
    // jpa, jackson
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.1.5")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    // querydsl
//    implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta")
    // h2 (test 코드용)
    runtimeOnly("com.h2database:h2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    // validation
    implementation("org.springframework.boot:spring-boot-starter-validation:3.1.5")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
