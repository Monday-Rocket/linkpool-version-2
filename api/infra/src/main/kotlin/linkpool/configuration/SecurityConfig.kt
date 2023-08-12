package linkpool.configuration

import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SecurityConfig {
    @Bean
    fun firebaseAuth(): FirebaseAuth {
        FirebaseApp.initializeApp()
        return FirebaseAuth.getInstance()
    }
}