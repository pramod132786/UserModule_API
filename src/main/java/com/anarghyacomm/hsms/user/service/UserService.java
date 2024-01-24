package com.anarghyacomm.hsms.user.service;

import java.util.List;
import java.util.Optional;

import javax.security.auth.login.AccountLockedException;

import org.springframework.http.ResponseEntity;

import com.anarghyacomm.hsms.user.entity.Doctor;
import com.anarghyacomm.hsms.user.entity.LoginForm;
import com.anarghyacomm.hsms.user.entity.UserEntity;

public interface UserService {
	
	public UserEntity login(LoginForm loginForm) throws AccountLockedException;
	
	public UserEntity save(UserEntity user);

	public Optional<UserEntity> getByEmail(String userEmail);

	public ResponseEntity<String> fetchDataFromDoctorMicroservice();

	public ResponseEntity<List<Doctor>> getAll();

	

}
