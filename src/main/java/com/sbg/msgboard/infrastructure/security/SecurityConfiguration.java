package com.sbg.msgboard.infrastructure.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Value("${security.issuer-uri}")
  private String issuerUri;

  @Value("${security.configuration.enabled}")
  private boolean securityEnabled;

  @Value("${security.configuration.allowed-origins}")
  private String allowedOrigins;

  @Override
  public void configure(HttpSecurity http) throws Exception {

    if (securityEnabled) {

      http.cors()
          .configurationSource(getCorsConfigurationSource())
          .and()
          .authorizeRequests(
              expressionInterceptUrlRegistry ->
                  expressionInterceptUrlRegistry
                      .antMatchers("/msgboard/api/v1")
                      .permitAll()
                      .anyRequest()
                      .authenticated())
          .oauth2ResourceServer()
          .jwt(jwt -> jwt.decoder(JwtDecoders.fromIssuerLocation(issuerUri)));

    } else {

      http.csrf().disable().authorizeRequests().antMatchers("/**").permitAll();
    }
  }

  private CorsConfigurationSource getCorsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(getAllowedOrigins());
    configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH"));
    configuration.setAllowCredentials(true);
    configuration.setAllowedHeaders(
        Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  private List<String> getAllowedOrigins() {

    // TODO: An exception might be thrown later if allowedOrigins is empty

    return Arrays.asList(allowedOrigins.split(","));
  }
}
