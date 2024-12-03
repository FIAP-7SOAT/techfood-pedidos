import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	val kotlinVersion = "1.9.24"

	kotlin("jvm") version kotlinVersion
	kotlin("plugin.noarg") version kotlinVersion
	kotlin("plugin.spring") version kotlinVersion
	kotlin("plugin.jpa") version kotlinVersion

	id("org.springframework.boot") version "3.2.5"
	id("io.spring.dependency-management") version "1.1.4"
	id("org.flywaydb.flyway") version "10.13.0"
	id("jacoco")
}

group = "br.com.fiap"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

val kotlinLoggingVersion = "3.0.5"
val mockkVersion = "1.13.11"
val springCloudVersion = "2023.0.1"
val springMockk = "4.0.2"

dependencies {
	modules {
		module("org.springframework.boot:spring-boot-starter-tomcat") {
			replacedBy("org.springframework.boot:spring-boot-starter-undertow", "Use Undertow instead of tomcat")
		}
	}
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("io.github.microutils:kotlin-logging:$kotlinLoggingVersion")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-aop")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-undertow")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.cloud:spring-cloud-starter")
	implementation("org.springframework.cloud:spring-cloud-starter-config")
	implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
	implementation("org.springframework.cloud:spring-cloud-starter-bootstrap")

	implementation("org.postgresql:postgresql:42.3.3")

	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")

	testImplementation("org.mockito.kotlin:mockito-kotlin:5.1.0")

	testImplementation("com.h2database:h2")
	testImplementation("com.ninja-squad:springmockk:$springMockk")
	testImplementation("com.tngtech.archunit:archunit-junit5:1.3.0")
	testImplementation("io.mockk:mockk:$mockkVersion")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.cloud:spring-cloud-contract-wiremock")
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:$springCloudVersion")
	}
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

jacoco {
	toolVersion = "0.8.8" // Especificar a versão do JaCoCo
}

tasks.jacocoTestReport {
	dependsOn("test") // Garantir que os testes sejam executados antes de gerar o relatório

	reports {
		xml.required.set(true) // Gerar relatório XML
		html.required.set(true) // Gerar relatório HTML
	}

	// Configurar os diretórios de classes com exclusões específicas
	classDirectories.setFrom(
		fileTree("$buildDir/classes/kotlin/main") {
			exclude(
				"**/app/**", // Pacote 'app'
				"**/dto/**", // Data Transfer Objects
				"**/mapper/**", // Mapeadores
				"**/configuration/**", // Configurações
				"**/generated/**", // Código gerado
				"**/web/advice/**", // Excluir o pacote web.advice
//				"**/exception/**", // Classes de exceção
//				"**/utils/**", // Classes utilitárias
//				"**/*Test*", // Excluir classes de teste
//				"**/core/domain/**", // Classes de domínio puro (caso sejam modelos)
//				"**/core/common/**", // Classes de uso geral ou comuns
				"**/core/domain/vo/**", // Classes de domínio puro (caso sejam modelos)
				"**/core/domain/"
			)
		}
	)
}

tasks.jacocoTestCoverageVerification {
	dependsOn(tasks.test)

	violationRules {
		rule {
			limit {
				minimum = 0.50.toBigDecimal() // Cobertura mínima de 80%
			}
		}
	}
}





