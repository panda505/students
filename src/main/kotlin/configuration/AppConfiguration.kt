package configuration

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import liquibase.integration.spring.SpringLiquibase
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.transaction.annotation.EnableTransactionManagement
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import services.GitHubService
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2
import javax.sql.DataSource

@SpringBootConfiguration
@EnableAutoConfiguration(exclude = [ErrorMvcAutoConfiguration::class])
@ComponentScan(basePackages = ["web", "repositories"])
@PropertySource("classpath:app.properties")
@EnableTransactionManagement
@EnableSwagger2
@Configuration
class AppConfiguration {

    @Bean
    fun dataSource(): DataSource {
        val config = HikariConfig()
        config.jdbcUrl = "jdbc:postgresql://localhost:5432/postgres"
        config.driverClassName = "org.postgresql.Driver"
        config.username = "testuser"
        config.password = "123qweASD"
        return HikariDataSource(config)
    }

    @Bean
    fun namedParameterJdbcTemplate(): NamedParameterJdbcTemplate {
        return NamedParameterJdbcTemplate(dataSource())
    }

    @Bean
    fun liquibase(): SpringLiquibase {
        val liquibase = SpringLiquibase()
        liquibase.setChangeLog("classpath:db/changeLog.xml")
        liquibase.setDataSource(dataSource())
        return liquibase
    }

    @Bean
    fun GitHubService(): GitHubService {
        val retrofit = Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        return retrofit.create(GitHubService::class.java)
    }

    fun apiInfo(): ApiInfo {
        return ApiInfoBuilder()
                .title("Student documentation")
                .description("")
                .build()
    }

    @Bean
    fun api(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
    }

}
