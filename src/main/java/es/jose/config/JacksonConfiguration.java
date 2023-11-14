package es.jose.config;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;

@Configuration
public class JacksonConfiguration {
	
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.setSerializationInclusion(Include.NON_NULL);
        
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        mapper.setDateFormat(df);
        mapper.setTimeZone(TimeZone.getTimeZone("Europe/Madrid"));
        
        SimpleModule module = new SimpleModule();
        module.addSerializer(java.sql.Date.class, new DateSerializer());
        mapper.registerModule(module);

        return mapper;
    }
	
}
