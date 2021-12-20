package com.nblog.config;

import io.swagger.annotations.Api;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

/**
 * @author liulin
 *
 * Swagger 相关配置：group、元信息
 *
 */

@Configuration
@EnableSwagger2
@Data
@ConfigurationProperties(prefix = "spring.swagger")
public class SwaggerConfig {

    private Boolean enable;

    @Bean
    // 每一个 Docket 是一个 Group
    public Docket docket(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .enable(enable)
                .groupName("liulin-v3.1")
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .build();
    }

    // 配置 swagger 元信息
    private ApiInfo apiInfo(){
        // 作者信息
        Contact contact = new Contact("CQU.liu", "https://liulin613.github.io/", "linliu@cqu.edu.cn");
        return new ApiInfo(
                "Nblog API",
                "即使再小的帆也能远航~",
                "v3.1",
                "http://nblog.org.cn/",
                contact,
                "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0",
                new ArrayList<>()
        );
    }
}
