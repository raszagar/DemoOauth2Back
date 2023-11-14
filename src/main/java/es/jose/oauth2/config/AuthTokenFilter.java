package es.jose.oauth2.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

public class AuthTokenFilter extends OncePerRequestFilter {
	
	private static final Logger log = LoggerFactory.getLogger(AuthTokenFilter.class);
 
    @Autowired
    @Qualifier("jwtDecoder")
    private JwtDecoder jwtDecoder;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);
            log.debug("Token: \"{}\"", jwt);
            
            validateJwtToken(jwt);
        } catch (Exception exception) {
            logger.error("Cannot set user authentication: {}", exception);
        }
 
        filterChain.doFilter(request, response);
    }
    
    private boolean validateJwtToken(String authToken) {
    	log.debug("validateJwtToken");
        try {
            jwtDecoder.decode(authToken);
            return true;
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        } catch (Exception e) {
			log.error("JWT Exception: {}", e.getMessage());
		}
 
        return false;
    }
 
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
 
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
 
        return null;
    }
}

