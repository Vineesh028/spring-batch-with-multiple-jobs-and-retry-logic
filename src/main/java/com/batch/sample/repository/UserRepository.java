package com.batch.sample.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.batch.sample.entity.UserEntity;



public interface UserRepository extends JpaRepository<UserEntity, String>{

}