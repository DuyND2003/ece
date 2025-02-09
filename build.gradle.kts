plugins {
	java
	id("org.springframework.boot") version "3.4.2"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.session:spring-session-core")
	compileOnly("org.projectlombok:lombok")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	implementation ("mysql:mysql-connector-java:8.0.33")
	annotationProcessor("org.projectlombok:lombok")

	// JSON Web Token (JWT)
	implementation("io.jsonwebtoken:jjwt-api:0.12.6")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")

	// Data Faker
	implementation("net.datafaker:datafaker:2.4.2")

	// Spring Boot Test (bao gồm JUnit 5 và Mockito)
	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(module = "junit")
	}
	testImplementation("org.springframework.security:spring-security-test")

	// Mockito
	testImplementation("org.mockito:mockito-core:5.2.0")
	testImplementation("org.mockito:mockito-junit-jupiter:5.2.0")

	// Redis
	implementation ("org.springframework.boot:spring-boot-starter-data-redis")
	implementation ("org.springframework.boot:spring-boot-starter-cache")


}

tasks.withType<Test> {
	useJUnitPlatform()
}
