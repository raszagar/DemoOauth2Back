package es.jose.oauth2.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityOauth2Config {
	
	private static final Logger log = LoggerFactory.getLogger(SecurityOauth2Config.class);
	
	@Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
	private String issuerUri;
	
	//@Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
	//private String jwkSetUri;
	
	@Autowired
	private AuthEntryPointJwt unauthorizedHandler;
	
	@Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable()
			//.exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
			//.and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.authorizeHttpRequests(authorize -> authorize
		 			.antMatchers("/ws/test/**").permitAll()
	        		.anyRequest().authenticated()
		 	)
	        .oauth2ResourceServer(
	        		oauth2  -> oauth2.jwt(
	        				jwt -> jwt.decoder(jwtDecoder())
	        		)
	        		.authenticationEntryPoint(unauthorizedHandler)
	        );
		
		//http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
		 
		return http.build();
	}
	
	@Bean(name = "jwtDecoder")
	public JwtDecoder jwtDecoder() {
		log.debug("jwtDecoder");
		
		//return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).jwsAlgorithm(SignatureAlgorithm.RS256).build();
		return JwtDecoders.fromIssuerLocation(issuerUri);
	}

}
