package com.example.partypurse.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SpringFoxConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(metaData())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.partypurse"))
                .paths(PathSelectors.ant("/**"))
                .build();
    }


    private ApiInfo metaData() {
        return new ApiInfoBuilder().title("PartyPurse").description("Dev build").termsOfServiceUrl("Братву на сисьски не меняют")
                .version("1.0").build();
    }
}
