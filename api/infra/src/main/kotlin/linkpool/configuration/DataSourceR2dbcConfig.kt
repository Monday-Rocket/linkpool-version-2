package linkpool.configuration

import io.r2dbc.spi.ConnectionFactory
import io.r2dbc.spi.ConnectionFactoryOptions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.core.io.ClassPathResource
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator
import org.springframework.r2dbc.core.DatabaseClient


@EnableR2dbcAuditing
@Configuration
class DataSourceR2dbcConfig {

    @Autowired
    private lateinit var env: Environment

    @Bean
    fun r2dbcDatabaseClient(connectionFactory: ConnectionFactory): DatabaseClient =
        DatabaseClient.builder().connectionFactory(connectionFactory).build()


    // schema generation (R2DBC에서 공식적으로 지원하지 않아 만듬)
    @Bean
    fun initializer(connectionFactory: ConnectionFactory): ConnectionFactoryInitializer? {
        val initializer = ConnectionFactoryInitializer()
        val resourceDatabasePopulator = ResourceDatabasePopulator()
        resourceDatabasePopulator.addScript(ClassPathResource("schema.sql"))
        resourceDatabasePopulator.addScript(ClassPathResource("data.sql"))
        initializer.setConnectionFactory(connectionFactory)
        initializer.setDatabasePopulator(resourceDatabasePopulator)
        return initializer
    }

}