package com.springboot3base.common.config;

import com.springboot3base.common.enums.RoleEnum;
import com.springboot3base.common.security.CustomAccessDeniedHandler;
import com.springboot3base.common.security.CustomAuthenticationEntryPoint;
import com.springboot3base.common.security.JwtAuthenticationFilter;
import com.springboot3base.common.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@RequiredArgsConstructor
@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;
    private final ApplicationContext applicationContext;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    protected SecurityFilterChain config(HttpSecurity http) throws Exception {
        return http
                .cors(cnf -> cnf.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .headers(cnf -> cnf.frameOptions(Customizer.withDefaults()).disable())
                .sessionManagement(cnf -> cnf.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(cnf ->
                        cnf.requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                                .requestMatchers(PERMIT_ALL_LIST).permitAll()
                                .requestMatchers(ADMIN_ONLY_LIST).hasAuthority(RoleEnum.ROLE_ADMIN.getTitle())
                                .anyRequest().authenticated())
                .exceptionHandling(cnf ->
                        cnf.accessDeniedHandler(new CustomAccessDeniedHandler())
                                .authenticationEntryPoint(new CustomAuthenticationEntryPoint()))
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    private static final String[] PERMIT_ALL_LIST = {
            "/*/auth/**",
            "/*/user/sign-up",
            "/*/user/find-id",
            "/*/user/find-pw",
            "/*/user/{id}/password",
            "/*/etc/health-check",
            "/v1/etc/**"
    };

    private static final String[] ADMIN_ONLY_LIST = {
            "/*/admin/**",
            "/*/user/resetPassword/{id}"
    };

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(IGNORING_LIST);
    }

    private static final String[] IGNORING_LIST = {
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger/**",
            "/actuator/health",
            "/*/exception/**"
    };

    CorsConfigurationSource corsConfigurationSource() {
        final var configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("https://*");
        configuration.addAllowedOriginPattern("http://*:*");
        configuration.addAllowedOriginPattern("https://*:*");
        configuration.addAllowedOriginPattern("*");
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("*"));
        final var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}