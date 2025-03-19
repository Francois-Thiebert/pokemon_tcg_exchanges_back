package pokemonTcgExchanges.configuration;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class ApiSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //@formatter:off
        return http.antMatcher("/api/**")
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .cors() // Activer la configuration CORS
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/api/**").permitAll()

                .antMatchers(HttpMethod.GET, "/api/user/login/check/*").permitAll()
                .antMatchers(HttpMethod.GET, "/api/*/adm/**").hasAnyRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/user/activityCheck").hasAnyRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/user/{id}").authenticated()
                .antMatchers(HttpMethod.GET, "/api/user/all/{id}").authenticated()
                .antMatchers(HttpMethod.GET, "/api/user/hasAskedUnblocking/{id}").authenticated()
                .antMatchers(HttpMethod.GET, "/api/user").hasAnyRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/auth").permitAll()
                .antMatchers(HttpMethod.GET).authenticated()
                
                .antMatchers(HttpMethod.POST, "/api/user").permitAll()
                .antMatchers(HttpMethod.POST).authenticated() 
                
                .antMatchers(HttpMethod.PUT).authenticated()
                .anyRequest().hasAnyRole("ADMIN")
//                    .anyRequest().permitAll()
                .and()
                .httpBasic()
                .and()
                .build();
        //@formatter:on
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://176.159.136.78:9000","http://echanges-pokemon-tcg.duckdns.org:9000","http://localhost:4200"));
        configuration.addAllowedMethod("*"); // Allow all HTTP methods
        configuration.addAllowedHeader("*"); // Allow all headers
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }



    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

