plugins {
    kotlin("jvm") version "2.2.21"
    kotlin("plugin.spring") version "2.2.21"
//    id("org.springframework.boot") version "4.0.0"
    id("org.springframework.boot") version "3.4.1"
    id("io.spring.dependency-management") version "1.1.7"
    kotlin("plugin.jpa") version "2.2.21"
    kotlin("kapt") version "2.2.21"

}

group = "com.grig"
version = "0.0.1-SNAPSHOT"
description = "RestApiRecipes"

java {
    toolchain {
//        languageVersion.set(JavaLanguageVersion.of(25))
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
//    maven("https://repo.spring.io/milestone") // иногда Spring Boot 4 еще в milestone
}

dependencies {
    // -------------------------
    // Core dependencies
    // -------------------------
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-security")

    // Hibernate Commons Annotations (для Hibernate 6.6.x)
//    implementation("org.hibernate.orm:hibernate-core")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    // PostgreSQL
    implementation("org.postgresql:postgresql:42.7.3")

    implementation("com.h2database:h2")

    // OpenAPI/Swagger
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.0")

    // JWT
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

    // Lombok
    kapt("org.projectlombok:lombok")
    compileOnly("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")

//    // Gson
//    implementation("com.google.code.gson:gson:2.10.1")

//    Если используешь старые версии Spring Boot / Netty, иногда нужно зависимость:
//    Это загружает нативный DNS резолвер для Mac, но чаще помогает первый вариант (use-jdk-dns).
//    implementation("io.netty:netty-resolver-dns-native-macos:4.1.103.Final")

// -------------------------
    // Test dependencies
    // -------------------------
    // Spring Boot тесты
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }

//    WebClient — это Spring WebFlux, даже если у тебя обычный MVC
    implementation("org.springframework.boot:spring-boot-starter-webflux")

    // Spring Security тесты
    testImplementation("org.springframework.security:spring-security-test")

    // JUnit5 Kotlin
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:2.2.21")

    // Mockito Kotlin
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.3.1")

    // MockK для Kotlin
    testImplementation("io.mockk:mockk:1.13.8")

    // Jackson Kotlin для тестов
    testImplementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.2")
//    БД H2 для тестов
    testImplementation("com.h2database:h2:2.2.220")
 }


kotlin {
//    jvmToolchain(25)
    jvmToolchain(21)

    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
    }
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
    useJUnitPlatform()
}




