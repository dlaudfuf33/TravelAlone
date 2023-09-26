package com.example.demo;

import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.SwaggerUiConfigParameters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// @Configuration 어노테이션은 이 클래스가 Spring의 Java Configuration 클래스임을 나타냅니다.
// 즉, 이 클래스를 통해 빈(Bean) 객체를 등록하거나 Spring 설정을 제공할 수 있습니다.
@Configuration
public class SwaggerConfig {

    // @Bean 어노테이션은 메서드가 Spring IoC 컨테이너에 의해 관리되는 빈 객체를 생성하고 제공한다는 것을 나타냅니다.
    // GroupedOpenApi는 OpenAPI의 하나 이상의 경로를 그룹화하는 데 사용되며, Swagger UI에서 이를 그룹으로 표시합니다.
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                // .group 메서드는 이 그룹의 이름을 설정합니다. 여기서는 "public"이라는 이름을 사용하였습니다.
                .group("public")
                // .pathsToMatch 메서드는 문서화하려는 API 경로를 지정합니다. "/api/**" 패턴은 /api로 시작하는 모든 엔드포인트를 포함합니다.
                .pathsToMatch("/api/**")
                .build();
    }

}
