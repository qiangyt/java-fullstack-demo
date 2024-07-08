package io.github.qiangyt.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * Security configuration for the main application.
 */
@EnableWebSecurity
@Configuration
@lombok.Getter
@lombok.Setter
public class SecurityConfig {

    @Value("${security.paths.sign-in:/rest/signin}")
    String signInPath;

    @Value("${security.paths.sign-up:/rest/signup}")
    String signUpPath;

    @Value("${security.paths.tokn:/rest/token}")
    String tokenPath;

    @Autowired
    SecurityMethods<Object> methods;

    @Autowired
    AuthService authService;

    @SuppressWarnings("removal")
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationConfiguration authConfig,
            ObjectMapper objectMapper) throws Exception {
        var publicPaths = new String[] { getTokenPath(), getSignUpPath(), getSignInPath(), "/rest/posts" };

        // http.authorizeHttpRequests(a ->
        // a.requestMatchers(publicPaths).permitAll().anyRequest().authenticated());
        http.authorizeHttpRequests(a -> {
            a.requestMatchers("/rest/signin").permitAll()
                    .requestMatchers("/rest/signup").permitAll()
                    .requestMatchers("/rest/token").permitAll()
                    .requestMatchers(HttpMethod.GET, "/rest/posts").permitAll()
                    .anyRequest().authenticated();
        });

        var jwtAuthFilter = new JwtAuthFilter(authConfig.getAuthenticationManager(), getAuthService(), objectMapper,
                getMethods());
        jwtAuthFilter.setFilterProcessesUrl(getSignInPath());

        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        http.cors();// disable(); // 启用CORS，禁用CSRF
        http.csrf().disable();// http.csrf(csrf ->
                              // csrf.ignoringRequestMatchers(publicPaths)).httpBasic(Customizer.withDefaults());
        http.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
        http.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.exceptionHandling(ex -> ex.authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
                .accessDeniedHandler(new BearerTokenAccessDeniedHandler()));
        return http.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOriginPatterns(Arrays.asList("*"));
        // config.setAllowedOrigins(Arrays.asList("http://192.168.6.93:7070"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("*"));
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

}
