package br.com.assembly.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.processing.Generated;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Generated(value ="io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-05-27T14:54:00.000Z[GMT]")
@Configuration
@EnableSwagger2
public class SwaggerDocumentationConfig {

    public ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("My Assembly")
                .description("API Assembly")
                .license("Apache 2.0")
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
                .version("1.0.0")
                .contact(new Contact("Danilo Santos", "www.ds2tecnologia.com.br", "danilosantos@ds2tecnologia.com.br"))
                .build();
    }

    @Bean
    public Docket customImplementation(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("br.com.assembly.web.controller"))
                .paths(PathSelectors.any())
                .build()
                .directModelSubstitute(LocalDate.class, java.sql.Date.class)
                .directModelSubstitute(OffsetDateTime.class, java.util.Date.class)
                .apiInfo(apiInfo());
    }
}
