package com.anarghyacomm.hsms.user.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.anarghyacomm.hsms.user.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>{
	
	public Optional<UserEntity> findByUserEmail(String userEmail);

}
