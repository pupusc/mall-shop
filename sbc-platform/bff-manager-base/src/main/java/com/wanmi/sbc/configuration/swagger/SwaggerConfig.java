package com.wanmi.sbc.configuration.swagger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * swagger config
 */
@Configuration
@EnableSwagger2
@ConditionalOnProperty(prefix = "swagger", name = "enable", havingValue = "true")
public class SwaggerConfig {

    @Value("${swagger.contact.name}")
    String contactName;

    @Value("${swagger.contact.url}")
    String contactUrl;

    @Value("${swagger.contact.email}")
    String contactEmail;

    @Value("${swagger.title}")
    String title;

    @Value("${swagger.description}")
    String description;

    @Value("${swagger.version}")
    String version;

    @Value("${swagger.enable}")
    boolean enable;

    @Value("${jwt.secret-key}")
    private String jwtSecretKey;


    /**
     * swagger通过注解表明该接口会生成文档，包括接口名、请求方法、参数、返回信息的等等。
     *
     * @return
     * @Api：修饰整个类，描述Controller的作用
     * @ApiOperation：描述一个类的一个方法，或者说一个接口
     * @ApiParam：单个参数描述
     * @ApiModel：用对象来接收参数
     * @ApiProperty：用对象接收参数时，描述对象的一个字段
     * @ApiResponse：HTTP响应其中1个描述
     * @ApiResponses：HTTP响应整体描述
     * @ApiIgnore：使用该注解忽略这个API
     * @ApiError：发生错误返回的信息
     * @ApiImplicitParam：一个请求参数
     * @ApiImplicitParams：多个请求参数paramType
     */
    @Bean
    public Docket createRestApi() {
        if (enable) {
            return new Docket(DocumentationType.SWAGGER_2)
                    .apiInfo(apiInfo())
                    .select()
                    .apis(RequestHandlerSelectors.basePackage("com.wanmi.sbc"))
                    .paths(PathSelectors.any())
                    .build()
                    // 配置header
                    .globalOperationParameters(setHeaderToken());
        } else {
            return new Docket(DocumentationType.SWAGGER_2)
                    .enable(enable)
                    .apiInfo(onlineApiInfo())
                    .select()
                    .paths(PathSelectors.none())
                    .build();
        }
    }

    /**
     * 站点描述信息
     *
     * @return
     */
    private ApiInfo apiInfo() {
        Contact contact = new Contact(contactName, contactUrl, contactEmail);
        return new ApiInfoBuilder()
                .title(title)
                .description(description)
                .contact(contact)
                .version(version)
                .build();
    }

    /**
     * 线上站点描述信息(不展示任何信息)
     *
     * @return
     */
    private ApiInfo onlineApiInfo() {
        Contact contact = new Contact("", "", "");
        return new ApiInfoBuilder()
                .title("")
                .description("")
                .contact(contact)
                .version("")
                .build();
    }

    /**
     * 设置header中的token,方便调试
     * @return
     */
    private List<Parameter> setHeaderToken() {
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<>();
        tokenPar.name("Authorization").description("接口需要的jwt token")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .defaultValue("Bearer ")
                .required(false)
                .build();
        pars.add(tokenPar.build());
        return pars;
    }
}
