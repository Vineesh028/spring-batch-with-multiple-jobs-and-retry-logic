package com.batch.sample.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.batch.sample.entity.CountryEntity;



public interface CountryRepository extends JpaRepository<CountryEntity, String>{

}