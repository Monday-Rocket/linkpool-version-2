package linkpool.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.channel.ExecutorChannel
import org.springframework.integration.config.EnableIntegration
import org.springframework.messaging.MessageChannel
import java.util.concurrent.Executors

@Configuration
@EnableIntegration
class IntegrationConfig {

  @Bean(name = ["signedOutEvent"])
  fun signedOutEventChannel(): MessageChannel {
    return ExecutorChannel(Executors.newFixedThreadPool(2))
  }
}