package com.getsmarter;

//import com.getsmarter.security.ConfigurationCryptPassword;
//import com.getsmarter.security.ConfigurtionSecurityApplication;
//import com.getsmarter.security.JwtFilter;
//import com.getsmarter.security.JwtService;
import org.apache.catalina.security.SecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableScheduling
@CrossOrigin(origins = "http://localhost:4200")
public class GetsmarterApplication {

	public static void main(String[] args) {
		SpringApplication.run(GetsmarterApplication.class, args);
	}

}
