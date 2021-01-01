package com.currencyexchangeratetracker.config;

import com.fasterxml.classmate.TypeResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    private static Logger LOGGER = LoggerFactory.getLogger(SwaggerConfig.class);
    private final TypeResolver typeResolver;
    @Bean
    public Docket productApi() {
        LOGGER.info("Swagger documentation");
        return new Docket(DocumentationType.SWAGGER_2)
                .select().apis(RequestHandlerSelectors.basePackage("com.currencyexchangeratetracker"))
                .build()
                .apiInfo(apiInfo());
    }
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Exchange rate request history API")
                .description("API to display exchange rates for given currencies")
               .version("1.0").build();
    }
    @Autowired
    public SwaggerConfig(TypeResolver typeResolver) {
        this.typeResolver = typeResolver;
    }
/*
    @Autowired
    public void createAdditionalModelDocumentation(Docket docket) {
        docket.additionalModels(typeResolver.resolve(Location.class),
                typeResolver.resolve(VehiclesInPolygon.class));

    }*/
}
