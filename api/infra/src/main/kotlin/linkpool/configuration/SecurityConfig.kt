package linkpool.configuration

import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import linkpool.security.FirebaseClient
import linkpool.security.LocalSecurityWebFilter
import linkpool.security.SecurityWebFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile


@Configuration
class SecurityConfig {
    @Profile("!local")
    @Bean
    fun firebaseAuth(): FirebaseAuth {
        FirebaseApp.initializeApp()
        return FirebaseAuth.getInstance()
    }

    @Profile("!local")
    @Bean
    fun firebaseClient(): FirebaseClient {
        return FirebaseClient(firebaseAuth())
    }

    @Profile("!local")
    @Bean
    fun securityWebFilter(): SecurityWebFilter {
        return SecurityWebFilter(firebaseClient())
    }

    @Profile("local")
    @Bean
    fun localSecurityWebFilter(): LocalSecurityWebFilter {
        return LocalSecurityWebFilter()
    }

}