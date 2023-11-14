package es.jose.controllers;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import es.jose.exception.MiException;

@RestController
@RequestMapping("/ws")
@CrossOrigin
public class AjaxController {
	
	private static final Logger log = LoggerFactory.getLogger(AjaxController.class);
	
	@Autowired
    @Qualifier("jwtDecoder")
    private JwtDecoder jwtDecoder;
	
	/**
	 * Este metodo no esta autenticado
	 * @return
	 */
	@GetMapping(value = "/test", produces = MediaType.APPLICATION_JSON_VALUE)
	public String test() {
		log.debug("test");
		return "\"Hola mundo!!\"";
	}
	
	@GetMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
	public String testAuth() {
		log.debug("testAuth");
		return "\"Hola mundo autenticado!!\"";
	}

	/**
	 * Obtener informacion del token
	 * 
	 * @param auth
	 * @return
	 * @throws MiException
	 */
	@GetMapping(value = "/saludo", produces = MediaType.APPLICATION_JSON_VALUE)
	public String getSaludo(
			@RequestHeader("Authorization") String auth) throws MiException {
		log.debug("getSaludo");
		try {
			String usuario = "";
			String token = auth.substring(7);
			log.debug("Token -> \"{}\"", token);
			
			Jwt jwtToken = jwtDecoder.decode(token);
			String uniqueName = jwtToken.getClaimAsString("unique_name");
			String name = jwtToken.getClaimAsString("name");
			if(name == null || name.isEmpty()) {
				usuario = uniqueName;
			} else {
				usuario = name;
			}
			
			return "\"Hola " + usuario + "!!\"";	
		} catch (Exception e) {
			throw new MiException("Error al obtener el saludo: " + e.getMessage(), e);
		}
	}
	
	@ExceptionHandler(MiException.class)
	public @ResponseBody
	String handleMiException(MiException e, HttpServletResponse response) {
		response.setContentType("text/html; charset=UTF-8");
		
		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	    
		return e.getMessage();
	}

}
