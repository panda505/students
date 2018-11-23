import configuration.AppConfiguration
import org.springframework.boot.SpringApplication

fun main(args: Array<String>) {
    val springApplication = SpringApplication(AppConfiguration::class.java)
    springApplication.run(*args)
}



