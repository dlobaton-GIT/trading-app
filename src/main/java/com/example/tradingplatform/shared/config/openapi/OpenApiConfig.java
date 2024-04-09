package com.example.tradingplatform.shared.config.openapi;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.LinkedHashMap;
import java.util.Map;


/**
 * Configuration class for Open API 3 documentation
 */
@Configuration
@ConfigurationProperties(prefix = "springdoc")
public class OpenApiConfig {

    /**
     * Configuration bean that describes Customer Offer API
     * @return object containing API info
     */
    @Bean
    public OpenAPI openAPI(final @Autowired Environment env) {
        return new OpenAPI()
                .info(new Info()
                        .title(env.getProperty("springdoc.docs.title"))
                        .description(env.getProperty("springdoc.docs.description"))
                        .version(env.getProperty("springdoc.docs.version"))
                        .termsOfService(env.getProperty("springdoc.docs.terms"))
                        .contact(new Contact().name("SIX Group"))
                        .license(new License()
                                .name("Trading API Terms and License")
                                .url("http://www.sixgroup.es/terms-of-service.html"))
                        .extensions(getApiExtensions(env)))
                .components(getGlobalSecurityScheme())
                .addSecurityItem(new SecurityRequirement().addList("BearerAuth"));
    }

    /**
     * Custom extensions shown inside info object
     * @param env environment info
     * @return map containing custom info
     */
    private static Map<String, Object> getApiExtensions(final Environment env)
    {
        Map<String, Object> extensions = new LinkedHashMap<>();
        extensions.put("x-ibm-name", env.getProperty("springdoc.docs.title"));
        extensions.put("x-sixgroup-catalogation", Map.of(
                "bian-landscape-version", StringUtils.EMPTY,
                "bian-business-area", StringUtils.EMPTY,
                "bian-business-domain", StringUtils.EMPTY,
                "bian-service-domain", StringUtils.EMPTY
        ));

        return extensions;
    }

    /**
     * Global security scheme configuration
     * @return security scheme component
     */
    private static Components getGlobalSecurityScheme()
    {
        return new Components()
                .addSecuritySchemes("BearerAuth",
                        new io.swagger.v3.oas.models.security.SecurityScheme()
                                .type(io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP)
                                .bearerFormat("JWT")
                                .scheme("bearer"));
    }
}
