plugins {
    kotlin("plugin.allopen")
}

allOpen {
    annotation("javax.transaction.Transactional")
    annotation("org.springframework.transaction.annotation.Transactional")
}

dependencies {
    implementation("org.springframework:spring-tx:5.3.27")
}
