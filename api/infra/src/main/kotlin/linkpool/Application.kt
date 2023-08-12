package linkpool

import linkpool.common.DomainComponent
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.scheduling.annotation.EnableAsync

@EnableAsync
@ConfigurationPropertiesScan
@SpringBootApplication
@ComponentScan(includeFilters = [ComponentScan.Filter(type = FilterType.ANNOTATION, value = [DomainComponent::class])])
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
