package com.getsmarter;

import com.getsmarter.entities.Role;
import com.getsmarter.entities.User;
import com.getsmarter.enums.Sexe;
import com.getsmarter.enums.TypeRole;
import com.getsmarter.repositories.UserRepo;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.apache.catalina.security.SecurityConfig;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableScheduling
@EnableCaching
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class GetsmarterApplication implements CommandLineRunner {

	UserRepo userRepo;
	PasswordEncoder passwordEncoder;
	public static void main(String[] args) {
		SpringApplication.run(GetsmarterApplication.class, args);
	}

	@Override
	@Transactional
	public void run(String... args) throws Exception {
		User admin = User.builder()
				.actif(true)
				.firstname("Ambang")
				.lastname("Ricky")
				.password(passwordEncoder.encode("Ricky@2024"))
				.email("ambangricky@gmail.com")
				.sexe(Sexe.HOMME)
				.phonenumber("699135252")
				.role(
						Role.builder()
								.libelle(TypeRole.ADMIN)
								.created_at(LocalDateTime.now())
								.build()
				)
				.created_at(LocalDateTime.now())
				.build();

		//On verifie si un utilisareur avec l'email existe deja
		Optional<User> optionalAdmin = this.userRepo.findByEmail("ambangricky@gmail.com");
		if (optionalAdmin.isPresent()){
			this.userRepo.deleteByEmail("ambangricky@gmail.com");
		}

		//On verifie si un utilisareur avec le numero de telephone existe deja
		Optional<User> optionalAdminPhoneNumber = this.userRepo.findByPhonenumber("699135252");
		if (optionalAdminPhoneNumber.isPresent()) {
			this.userRepo.deleteByPhonenumber("699135252");
		}

		this.userRepo.save(admin);
	}
}
