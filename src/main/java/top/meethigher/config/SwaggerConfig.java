package top.meethigher.config;

import io.swagger.annotations.Api;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;

/**
 * SwaggerConfig
 *
 * @author chenchuancheng
 * @github https://github.com/meethigher
 * @blog https://meethigher.top
 * @time 2022/3/6 05:43
 */
@Configuration
public class SwaggerConfig {
    //配置swagger的实例
    @Bean
    public Docket docket(Environment environment) {
        //获取项目环境
//        Profiles profiles = Profiles.of("dev");
//        boolean flag = environment.acceptsProfiles(profiles);
        boolean flag = true;
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .enable(flag)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.any())
                .build();
    }

    //配置swagger信息
    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Swagger Api文档",
                "代码改变世界！",
                "1.0",
                "https://127.0.0.1:9090/swagger-ui/index.html",
                new Contact("meethigher", "https://meethigher.top", "meethigher@qq.com"),
                "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0", new ArrayList());
    }
}
