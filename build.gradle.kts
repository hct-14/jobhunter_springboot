plugins {
	java
	id("org.springframework.boot") version "3.3.0"
	id("io.spring.dependency-management") version "1.1.5"
	id("io.freefair.lombok") version "8.6"
}

group = "job_hunter"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
	implementation("com.turkraft.springfilter:jpa:3.1.7")
 	runtimeOnly("com.mysql:mysql-connector-j")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	implementation ("org.springframework.boot:spring-boot-starter-security")
	implementation ("org.springframework.boot:spring-boot-starter-validation")
//	implementation ("org.springframework.boot:spring-boot-starter-lombok")

	implementation ("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
//	compileOnly 'org.projectlombok:lombok:1.18.26'
//	annotationProcessor 'org.projectlombok:lombok:1.18.26'
//	testCompileOnly 'org.projectlombok:lombok:1.18.26'
//	testAnnotationProcessor 'org.projectlombok:lombok:1.18.26'
//	implementation ("io.github.jhipster:jhipster")


}

tasks.withType<Test> {
	useJUnitPlatform()
}
