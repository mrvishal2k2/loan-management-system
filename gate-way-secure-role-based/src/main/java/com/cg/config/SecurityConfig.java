package com.cg.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import com.cg.filter.JwtAuthenticationFilter;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
            .authorizeExchange(exchanges -> exchanges
            		
            		
            		.pathMatchers("/v3/api-docs/**").permitAll()
            		.pathMatchers("/swagger-ui.html").permitAll()
            		.pathMatchers("/swagger-ui/**","webjars/swagger-ui.html").permitAll()

                    
           //     .pathMatchers("/public/**").permitAll()   // Public routes, no auth required
                .pathMatchers(HttpMethod.POST,"/identity-service/auth/login", "/identity-service/auth/register").permitAll() // Allow access without authentication
              
                
        // customer-service
        .pathMatchers(HttpMethod.GET,"/customer-service/api/customers/**").hasRole("ROLE_user") 
        .pathMatchers(HttpMethod.POST,"/customer-service/api/customers/**").hasRole("ROLE_user")    
        .pathMatchers(HttpMethod.PUT,"/customer-service/api/customers/update/kyc/**").hasRole("ROLE_admin")
        .pathMatchers(HttpMethod.PUT,"/customer-service/api/customers/**").hasRole("ROLE_user")
        .pathMatchers(HttpMethod.DELETE,"/customer-service/api/customers/**").hasRole("ROLE_user")

        
        // loan-service
        .pathMatchers(HttpMethod.GET,"/loan-service/loan/type/**").hasRole("ROLE_user") 
        .pathMatchers(HttpMethod.POST,"/loan-service/loan/type/**").hasRole("ROLE_admin") 

        .pathMatchers(HttpMethod.GET,"/loan-service/loan/application/**").hasRole("ROLE_user") 
        .pathMatchers(HttpMethod.POST,"/loan-service/loan/application/**").hasRole("ROLE_user") 
        .pathMatchers(HttpMethod.PUT,"/loan-service/loan/application/**").hasRole("ROLE_user") 
        .pathMatchers(HttpMethod.PUT,"/loan-service/loan/application/status/**").hasRole("ROLE_admin") 
        
        .pathMatchers(HttpMethod.PUT,"/loan-service/loan/approval/**").hasRole("ROLE_admin")  
        .pathMatchers(HttpMethod.POST,"/loan-service/loan/approval/**").hasRole("ROLE_admin")  
        
        // payment-service
        .pathMatchers(HttpMethod.GET,"/payment-service/api/payment/**").hasRole("ROLE_admin")  
        .pathMatchers(HttpMethod.POST,"/payment-service/api/payment/**").hasRole("ROLE_user")  
        .pathMatchers(HttpMethod.GET,"/payment-service/api/payment/status/**").hasRole("ROLE_user")  
        .pathMatchers(HttpMethod.GET,"/payment-service/api/payment/loan/**").hasRole("ROLE_user")  
        
        
        // gateway
        .pathMatchers(HttpMethod.GET,"/report-service/report/**").hasRole("ROLE_user")
        
        .pathMatchers(HttpMethod.GET,"/notification-service/notify/**").hasRole("ROLE_admin")
        
        .anyExchange().authenticated()              // All other routes require authentication
            )
            .addFilterAt(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION) // Add JWT filter
            .csrf().disable()  // Disable CSRF for stateless APIs
            .build();
    }
}
