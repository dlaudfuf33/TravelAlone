package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    /**
     * AuthenticationManager 빈을 생성하고 등록합니다.
     * Spring Security에서 인증 처리에 사용됩니다.
     *
     * @return AuthenticationManager 빈
     * @throws Exception 예외 발생 시
     */
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    /**
     * HTTP 보안 설정을 구성합니다.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // CSRF 보호 비활성화
                .cors()  // CORS 설정 활성화
                .configurationSource(corsConfigurationSource())  // CORS 설정을 가져옴
                .and()
                .authorizeRequests()
                .antMatchers("/public/**", "/api/*").permitAll() // "/api/signup" 추가
                .antMatchers("/admin/**").hasRole("ADMIN")  // /admin/** 경로는 ADMIN 역할을 가진 사용자만 접근 허용
                .antMatchers("/**").permitAll() // 모든 경로에 대한 접근을 허용 (개발 중일 때만 사용하길 권장)
                .anyRequest().authenticated()  // 위에 정의되지 않은 나머지 요청은 인증된 사용자만 접근 가능
                .and()
                .formLogin().disable()  // 폼 기반 로그인 비활성화
                .logout()
                .permitAll();  // 모든 사용자에게 로그아웃 접근 허용
    }

    /**
     * CORS 설정을 정의합니다.
     *
     * @return UrlBasedCorsConfigurationSource
     */
    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:3000");  // 허용할 Origin 설정
        config.addAllowedMethod("*");  // 모든 HTTP 메서드 허용
        config.addAllowedHeader("*");  // 모든 HTTP 헤더 허용
        config.setAllowCredentials(true);  // credentials 허용 설정

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);  // 모든 경로에 대해 CORS 설정 적용
        return source;
    }
    /**
     * 사용자 인증을 처리하는 데 사용될 사용자 서비스 및 비밀번호 암호화를 구성합니다.
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(customUserDetailsService)  // 주입 받은 customUserDetailsService 사용
                .passwordEncoder(passwordEncoder());
    }

    /**
     * 비밀번호 암호화를 위한 PasswordEncoder 빈을 등록합니다.
     *
     * @return PasswordEncoder 빈
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
