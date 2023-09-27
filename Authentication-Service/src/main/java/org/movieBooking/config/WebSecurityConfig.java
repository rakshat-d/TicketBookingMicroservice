package org.movieBooking.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.movieBooking.filters.ExceptionHandlerFilter;
import org.movieBooking.security.ApplicationUserDetailsService;
import org.movieBooking.security.JWTAuthenticationFilter;
import org.movieBooking.security.JWTVerifierFilter;
import org.movieBooking.services.TokensRedisService;
import org.movieBooking.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private ApplicationUserDetailsService applicationUserDetailsService;

	@Autowired
	private TokensRedisService redisService;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	private UserService userService;

	@Autowired
	@Lazy
	private AuthenticationManager authenticationManager;

	@Autowired
	@Qualifier("restAuthenticationEntryPoint")
	AuthenticationEntryPoint entryPoint;
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception 
	{
			http.csrf(AbstractHttpConfigurer::disable);
			http.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
			http.exceptionHandling(ex -> ex.authenticationEntryPoint(entryPoint));
			http.addFilter(new JWTAuthenticationFilter(authenticationManager, redisService));
			http.addFilterAfter(new JWTVerifierFilter(redisService, userService), JWTAuthenticationFilter.class)
			.authorizeHttpRequests((auth) -> auth.requestMatchers("/create").permitAll())
			.authorizeHttpRequests((auth) -> auth.requestMatchers("/actuator/**").permitAll())
	        .authorizeHttpRequests((auth)->auth.
							anyRequest().authenticated());
	        return http.build();
	}
}