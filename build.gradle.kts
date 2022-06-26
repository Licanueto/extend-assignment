import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.7.0"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	war
	kotlin("jvm") version "1.6.21"
	kotlin("plugin.spring") version "1.6.21"
}

group = "com.paywithextend"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("com.fasterxml.jackson.core:jackson-databind:2.13.3")
	implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
	implementation("io.github.microutils:kotlin-logging-jvm:2.1.23")
	implementation("ch.qos.logback:logback-classic:1.2.11")
	implementation("org.junit.jupiter:junit-jupiter:5.8.1")

	developmentOnly("org.springframework.boot:spring-boot-devtools")
	providedRuntime("org.springframework.boot:spring-boot-starter-tomcat")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.projectreactor:reactor-test")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
