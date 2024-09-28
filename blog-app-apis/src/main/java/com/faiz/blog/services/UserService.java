package com.faiz.blog.services;

import java.util.List;

import com.faiz.blog.payloads.UserDto;

public interface UserService {

	UserDto registerUser(UserDto userDto);
	UserDto createUser(UserDto userDto);
	UserDto updateUser(UserDto userDto, int id);
	UserDto getUserById(int id);
	List<UserDto> getAllUsers();
	void deleteUser(int id);
}
