package com.faiz.blog;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.faiz.blog.config.AppConstants;
import com.faiz.blog.entities.Role;
import com.faiz.blog.repositories.RoleRepository;

@SpringBootApplication
public class BlogAppApisApplication implements CommandLineRunner{
	
	@Autowired
	private PasswordEncoder password;
	
	@Autowired
	private RoleRepository roleRepository;

	public static void main(String[] args) {
		SpringApplication.run(BlogAppApisApplication.class, args);
	}
	
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Override
	public void run(String... args) throws Exception {
		
		System.out.println(password.encode("abc"));
		
		try {
			
			Role role = new Role();
			role.setId(AppConstants.Role_Admin);
			role.setName("ROLE_ADMIN");
			
			Role role1 = new Role();
			role1.setId(AppConstants.Role_User);
			role1.setName("ROLE_USER");
			
			List<Role> list = List.of(role, role1);
			List<Role> savedRoles= this.roleRepository.saveAll(list);
			
			savedRoles.forEach(r->{
				System.out.println(r.getName());
				});
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
