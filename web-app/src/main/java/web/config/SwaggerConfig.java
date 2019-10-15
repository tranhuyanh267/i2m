package web.config;

import com.google.common.collect.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    //Swagger UI run at: <host:port>/swagger-ui.html
    //Example: http://localhost:8080/swagger-ui.html

    private ApiKey apiKey() {
        return new ApiKey("apiKey", "Authorization", "header");
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage("web"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(metaData())
                .securitySchemes(Lists.newArrayList(apiKey()));
    }

    private ApiInfo metaData() {
        return new ApiInfoBuilder()
                .title("I2M Swagger UI")
                .description("This is the description. Have fun.")
                //Add Contact like this
                //.contact(new Contact("I2M API V1", "i2m.com", "i2m@gmail.com"))
                .build();
    }
}
