package com.faiz.blog.controllers;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.faiz.blog.entities.User;
import com.faiz.blog.exceptions.LoginException;
import com.faiz.blog.payloads.UserDto;
import com.faiz.blog.security.CustomUserDetailService;
import com.faiz.blog.security.JwtRequest;
import com.faiz.blog.security.JwtResponse;
import com.faiz.blog.security.JwtTokenHelper;
import com.faiz.blog.services.UserService;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin("*")
public class AuthenticationController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	CustomUserDetailService customeUser;
	
	@Autowired
	private JwtTokenHelper jwtTokenHelper;
	
	@Autowired
	private UserService userService;
	
//	generate token
	
	@PostMapping("/generate-token")
	public ResponseEntity<?> generateToken(@RequestBody JwtRequest jwtRequest) throws Exception
	{
		try {
			this.authenticate(jwtRequest.getUsername(), jwtRequest.getPassword());
		} catch (UsernameNotFoundException e) {
			
			e.printStackTrace();
			throw new Exception("User not found");
		}
		UserDetails userDetails = this.customeUser.loadUserByUsername(jwtRequest.getUsername());
		String token = this.jwtTokenHelper.generateToken(userDetails);
		return ResponseEntity.ok(new JwtResponse(token));
		
	}
	
	private void authenticate(String username, String password) throws Exception
	{
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			
			throw new Exception("User is disabled"+e.getMessage());
		} catch (BadCredentialsException e) {
			
			throw new LoginException("Invalid credentials");
		}
	}
//	return the details of user who is logged in
	@GetMapping("/current-user")
	public User getCurrentUser(Principal principal) {
		return (User) this.customeUser.loadUserByUsername(principal.getName());
	}
	
	@PostMapping("/register")
	public ResponseEntity<UserDto> registerUser(@Valid @RequestBody UserDto userDto){
		UserDto registerUserDto = this.userService.registerUser(userDto);
		return new ResponseEntity<>(registerUserDto, HttpStatus.CREATED);
	}

}
