plugins {
    java
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"
    id("org.jetbrains.kotlin.jvm") version "2.0.21"
    id("org.jetbrains.kotlin.plugin.jpa") version "2.0.21"
    id("org.jetbrains.kotlin.kapt") version "2.0.21"
}

group = "edu.example"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.3.2")
    implementation("mysql:mysql-connector-java:8.0.33")
    implementation("com.zaxxer:HikariCP:5.1.0")

    // Querydsl은 Kotlin의 kapt로만 설정
    kapt("com.querydsl:querydsl-apt:5.1.0:jakarta")
    implementation("com.querydsl:querydsl-jpa:5.1.0:jakarta")

    implementation("org.springframework.boot:spring-boot-starter-security:3.2.5")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf:3.3.3")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")

    // JSON Web Token (JWT) 관련 라이브러리
    implementation("io.jsonwebtoken:jjwt-api:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")

    // Spring Security OAuth2 Client
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")

    compileOnly("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    annotationProcessor("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")

    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // 테스트용 라이브러리
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-starter-data-jpa")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testRuntimeOnly("com.h2database:h2:2.3.232")

    // Redis와 이메일을 위한 의존성
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-mail")
}

// Querydsl Q 클래스 생성 파일 경로 설정
val querydslDir = file("src/main/generated")

sourceSets["main"].java.srcDir(querydslDir)

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-s")
    options.compilerArgs.add(querydslDir.path)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "17"
    }
}

tasks.clean {
    delete(querydslDir)
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
