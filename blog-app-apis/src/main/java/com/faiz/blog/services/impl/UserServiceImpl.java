package com.faiz.blog.services.impl;

// Tata Nifty Midcap 150 Momentum 50 Index Fund - Gr
// Kotak Nifty 200 Momentum 30 Index Fund - Gr

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.faiz.blog.config.AppConstants;
import com.faiz.blog.entities.Role;
import com.faiz.blog.entities.User;
import com.faiz.blog.exceptions.ResourceNotFoundException;
import com.faiz.blog.payloads.UserDto;
import com.faiz.blog.repositories.RoleRepository;
import com.faiz.blog.repositories.UserRepository;
import com.faiz.blog.services.UserService;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Override
	public UserDto createUser(UserDto userDto) {
		
		User user = this.dtoToUser(userDto);
		User savedUser = this.userRepository.save(user);
		
		return this.userToDto(savedUser);
	}

	@Override
	public UserDto updateUser(UserDto userDto, int id) {
		
		User user = this.userRepository.findById(id).orElseThrow( () -> new ResourceNotFoundException("User", "id", id));
		user.setName(userDto.getName());
		user.setEmail(userDto.getEmail());
		user.setPassword(userDto.getPassword());
		user.setAbout(userDto.getAbout());
		User updatedUser = this.userRepository.save(user);
		return this.userToDto(updatedUser);
	}

	@Override
	public UserDto getUserById(int id) {
		
		User user = this.userRepository.findById(id).orElseThrow( () -> new ResourceNotFoundException("User", "id", id));
		return this.userToDto(user);
	
	}

	@Override
	public List<UserDto> getAllUsers() {
		
		List<User> users = this.userRepository.findAll();
		List<UserDto> userDtos =  users.stream().map(user -> this.userToDto(user)).collect(Collectors.toList());
		return userDtos;
	}

	@Override
	public void deleteUser(int id) {
		
		User user = this.userRepository.findById(id).orElseThrow( () -> new ResourceNotFoundException("User", "id", id));
		this.userRepository.delete(user);
	}
	
	private User dtoToUser(UserDto userDto) {
		
		User user = this.modelMapper.map(userDto, User.class);
//		user.setId(userDto.getId());
//		user.setName(userDto.getName());
//		user.setEmail(userDto.getEmail());
//		user.setPassword(userDto.getPassword());
//		user.setAbout(userDto.getAbout());
		
		return user;
		
	}
	
	private UserDto userToDto(User user) {
		
		UserDto userDto = this.modelMapper.map(user, UserDto.class);
//		userDto.setId(user.getId());
//		userDto.setName(user.getName());
//		userDto.setEmail(user.getEmail());
//		userDto.setPassword(user.getPassword());
//		userDto.setAbout(user.getAbout());
		
		return userDto;
	}

	@Override
	public UserDto registerUser(UserDto userDto) {
		User user = this.dtoToUser(userDto);
		user.setPassword(this.passwordEncoder.encode(user.getPassword()));
		Role role = this.roleRepository.findById(AppConstants.Role_User).get();
		user.getRoles().add(role);
		User savedUser = this.userRepository.save(user);
		return this.userToDto(savedUser);
	}

}
