plugins {
  java
  kotlin("jvm")
  kotlin("plugin.spring")
  id("org.springframework.boot")
  id("io.spring.dependency-management")
}

dependencies {
  implementation(project(":api:domain"))
  implementation("org.springframework.boot:spring-boot-starter-actuator")
  implementation("org.springframework.boot:spring-boot-starter-webflux")
  implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
  implementation("org.springframework.security:spring-security-core")

  // firebase
  implementation("com.google.firebase:firebase-admin:_")

  // reactor tool
  implementation("io.projectreactor:reactor-tools")

  // r2dbc
  implementation("io.asyncer:r2dbc-mysql:0.9.2")

  // QueryDSL
  implementation("com.querydsl:querydsl-core:5.0.0")
  implementation("com.querydsl:querydsl-sql:5.0.0")
  implementation("com.querydsl:querydsl-sql-spring:5.0.0")

  // rxjava
  implementation("io.reactivex.rxjava3:rxjava:_")

  // kotlin
  implementation("org.jetbrains.kotlin:kotlin-reflect:_")

  testImplementation("org.springframework.boot:spring-boot-starter-test") {
    exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    exclude(group = "org.mockito")
  }
  testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")

  // Swagger
  implementation("io.springfox:springfox-boot-starter:3.0.0")

  // coroutine
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:_")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:_")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-rx3:_")

  // json
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

  // integration
  implementation("org.springframework.integration:spring-integration-event:6.1.2")
}

allOpen{
  annotation("org.springframework.transaction.annotation.Transactional")
}