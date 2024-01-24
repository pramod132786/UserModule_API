//package com.anarghyacomm.hsms.user.service;
//
//import java.util.Base64;
//import java.util.Base64.Encoder;
//import java.util.Optional;
//
//import javax.security.auth.login.AccountLockedException;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.anarghyacomm.hsms.user.entity.LoginForm;
//import com.anarghyacomm.hsms.user.entity.UserEntity;
//import com.anarghyacomm.hsms.user.exceptions.UserNotFoundException;
//import com.anarghyacomm.hsms.user.repo.UserRepository;
//
//@Service
//public class UserServiceImpl implements UserService {
//	
//	@Autowired
//	private UserRepository userRepo;
//
//	@Override
//	public UserEntity login(LoginForm loginForm) throws AccountLockedException {
//		Optional<UserEntity> byUserEmail = userRepo.findByUserEmail(loginForm.getUserEmail());
//		if (byUserEmail.isPresent()) {
//			UserEntity userEntity = byUserEmail.get();
//			String pazwd = loginForm.getPazzwd();
////
////			Encoder encoder = Base64.getEncoder();
////			String getpassword = encoder.encodeToString(pazwd.getBytes());
//
//			String encodedPassword = byUserEmail.get().getPazzwd();
//
//			if (userEntity.getAccountStatus().contains("Deactive")) {
//				throw new AccountLockedException("Your account is deactivated . Please contact support Team.");
//
//			}
////
////			if (userEntity.getFailedLoginAttempts() >= 4) {
////				userEntity.setAccountStatus("Locked");
////				userRepo.save(userEntity);
////				throw new AccountLockedException("Account is locked. Please contact support Team.");
////			}
//
//			if (pazwd.equals(encodedPassword)) {
//
//				userEntity.setFailedLoginAttempts(0);
//				userRepo.save(userEntity);
//				return userEntity;
//
//			} else {
//
//				userEntity.setFailedLoginAttempts(userEntity.getFailedLoginAttempts() + 1);
//				userRepo.save(userEntity);
//				throw new UserNotFoundException("You have entered an incorrect Credentails.");
//			}
//		} else {
//			throw new UserNotFoundException("Invalid Credentails");
//		}
//	}
//
//	@Override
//	public UserEntity save(UserEntity user) {
//		// TODO Auto-generated method stub
//		 user.setAccountStatus("active");
//		return userRepo.save(user);
//	}
//
//	 public Optional<UserEntity> getByEmail(String userEmail) {
//		 return userRepo.findByUserEmail(userEmail);
//	 }
//}

package com.anarghyacomm.hsms.user.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.security.auth.login.AccountLockedException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.anarghyacomm.hsms.user.entity.Doctor;
import com.anarghyacomm.hsms.user.entity.LoginForm;
import com.anarghyacomm.hsms.user.entity.UserEntity;
import com.anarghyacomm.hsms.user.exceptions.UserNotFoundException;
import com.anarghyacomm.hsms.user.logindto.LoginDto;
import com.anarghyacomm.hsms.user.repo.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepo;

//    @Autowired
    private RestTemplate restTemplate; 
    
    @Autowired
    public UserServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    
    List<Doctor> doctor = new ArrayList<Doctor>();
    
    public ResponseEntity<List<Doctor>> getAll() {
        String doctorMicroserviceUrl = "http://localhost:9000/doctor/all";

        ResponseEntity<List<Doctor>> response = restTemplate.exchange(
                doctorMicroserviceUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Doctor>>() {}
        );
        
    doctor = response.getBody();
        return response;
    }
    

//    public ResponseEntity<String> commonLogin(LoginDto dto) {
//        // Check if the list of doctors is not null
//    	String doctorMicroserviceUrl = "http://localhost:9000/doctor/all";
//
//        ResponseEntity<List<Doctor>> response = restTemplate.exchange(
//                doctorMicroserviceUrl,
//                HttpMethod.GET,
//                null,
//                new ParameterizedTypeReference<List<Doctor>>() {}
//        );
//        
//    doctor = response.getBody();
//        if (doctor != null) {
//            for (Doctor doctors : doctor) {
//                String doctorEmail = doctors.getEmail();
//                // Trim email for comparison
//                if (dto.getEmail().trim().equals(doctorEmail.trim())) {
//                    String doctorPassword = doctors.getPazz();
//                    // Trim password for comparison
//                    if (dto.getPazz().trim().equals(doctorPassword.trim())) {
//                        return ResponseEntity.ok("Doctor login success");
//                    }
//                }
//            }
//        }
//
//        Optional<UserEntity> userOptional = userRepo.findByUserEmail(dto.getEmail());
//
//        if (userOptional.isPresent()) {
//            UserEntity user = userOptional.get();
//            String userPassword = user.getPazzwd();
//
//            // Trim password for comparison
//            if (dto.getPazz().trim().equals(userPassword.trim())) {
//                return ResponseEntity.ok("User login success");
//            }
//        }
//
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login Fail");
//    }
    
    
    
    public ResponseEntity<Map<String, Object>> commonLogin(LoginDto dto) {
        // ... (your existing code)
    	String doctorMicroserviceUrl = "http://localhost:9000/doctor/all";
    	
    	        ResponseEntity<List<Doctor>> response = restTemplate.exchange(
    	                doctorMicroserviceUrl,
    	                HttpMethod.GET,
    	                null,
    	                new ParameterizedTypeReference<List<Doctor>>() {}
    	        );
    	        doctor = response.getBody();

        if (doctor != null) {
            for (Doctor doctors : doctor) {
                String doctorEmail = doctors.getEmail();
                if (dto.getEmail().trim().equals(doctorEmail.trim())) {
                    String doctorPassword = doctors.getPazz();
                    if (dto.getPazz().trim().equals(doctorPassword.trim())) {
                        Map<String, Object> successResponse = new HashMap<>();
                        successResponse.put("message", "Doctor login success");
                        successResponse.put("data", doctors);
                        return ResponseEntity.ok(successResponse);
                    }
                }
            }
        }

        Optional<UserEntity> userOptional = userRepo.findByUserEmail(dto.getEmail());

        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            String userPassword = user.getPazzwd();

            if (dto.getPazz().trim().equals(userPassword.trim())) {
                Map<String, Object> successResponse = new HashMap<>();
                successResponse.put("message", "User login success");
                successResponse.put("data", user);
                return ResponseEntity.ok(successResponse);
            }
        }

        Map<String, Object> failureResponse = new HashMap<>();
        failureResponse.put("message", "Login Fail");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(failureResponse);
    }
    

    @Override
    public UserEntity login(LoginForm loginForm) throws AccountLockedException {
        Optional<UserEntity> byUserEmail = userRepo.findByUserEmail(loginForm.getUserEmail());
        if (byUserEmail.isPresent()) {
            UserEntity userEntity = byUserEmail.get();
            String pazwd = loginForm.getPazzwd();

            String encodedPassword = byUserEmail.get().getPazzwd();

            if (userEntity.getAccountStatus().contains("Deactive")) {
                throw new AccountLockedException("Your account is deactivated. Please contact support Team.");
            }

            if (pazwd.equals(encodedPassword)) {
                userEntity.setFailedLoginAttempts(0);
                userRepo.save(userEntity);
                return userEntity;
            } else {
                userEntity.setFailedLoginAttempts(userEntity.getFailedLoginAttempts() + 1);
                userRepo.save(userEntity);
                throw new UserNotFoundException("You have entered an incorrect Credentials.");
            }
        } else {
            throw new UserNotFoundException("Invalid Credentials");
        }
    }
    
    
    
    

    public ResponseEntity<String> fetchDataFromDoctorMicroservice() {
        String doctorMicroserviceUrl = "http://localhost:9000/doctor/all";
        return restTemplate.exchange(doctorMicroserviceUrl, HttpMethod.GET, null, String.class);
    }
    
    
    
    
    @Override
    public UserEntity save(UserEntity user) {
    	
        user.setAccountStatus("active");
        return userRepo.save(user);
    }
    
    

    @Override
    public Optional<UserEntity> getByEmail(String userEmail) {
        return userRepo.findByUserEmail(userEmail);
    }
    
   

}

