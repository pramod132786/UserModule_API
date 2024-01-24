package com.anarghyacomm.hsms.user.contoller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.security.auth.login.AccountLockedException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anarghyacomm.hsms.user.entity.Doctor;
import com.anarghyacomm.hsms.user.entity.LoginForm;
import com.anarghyacomm.hsms.user.entity.UserEntity;
import com.anarghyacomm.hsms.user.exceptions.UserNotFoundException;
import com.anarghyacomm.hsms.user.logindto.LoginDto;
import com.anarghyacomm.hsms.user.service.UserService;
import com.anarghyacomm.hsms.user.service.UserServiceImpl;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserServiceImpl userServiceImple;

	@SuppressWarnings("unlikely-arg-type")
	@PostMapping("/user/login")
	public ResponseEntity<UserEntity> login(@RequestBody LoginForm loginForm) throws UserNotFoundException, AccountLockedException {
		System.out.println("Entered");
		UserEntity userLogin = userService.login(loginForm);
		try { 

			if (userLogin.equals("You have entered an incorrect Credentails.")) {
				return new ResponseEntity<>(userLogin, HttpStatus.UNAUTHORIZED);

			} else {
				return new ResponseEntity<>(userLogin, HttpStatus.OK);
			}
		} catch (UserNotFoundException e) {
			return new ResponseEntity<>(userLogin, HttpStatus.FORBIDDEN);

		}

	}
	
	@PostMapping("/user/save")
	public UserEntity Register(@RequestBody UserEntity user ) {
		 userService.save(user);
		return user;
		
	}
	
	@GetMapping("/User/getBYMail/{userEmail}")
   public Optional<UserEntity> getByUserMail(@PathVariable  String userEmail) {
		
	return userService.getByEmail(userEmail);
	   
   }
   
	@GetMapping("/getDoctor")
	public ResponseEntity<List<Doctor>> getAll() {
		
		return userService.getAll() ;
		
	}
	
	@PostMapping("/commonLogin")  // Assuming it's a POST request, change as needed
	public ResponseEntity<Map<String, Object>>  commonLogin(@RequestBody LoginDto dto) {
		return  userServiceImple.commonLogin(dto) ;
	    // Your logic here
	}

	
	
}
