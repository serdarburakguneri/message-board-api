package com.sbg.msgboard.infrastructure.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.jwt.JwtDecoders;

@Configuration
@ConditionalOnProperty(prefix = "security", name = "enabled", havingValue = "true")
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Value("${security.issuer-uri}")
  private String issuerUri;

  @Override
  public void configure(HttpSecurity http) throws Exception {
    http.cors()
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
  }
}
