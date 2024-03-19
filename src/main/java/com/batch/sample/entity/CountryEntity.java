package com.batch.sample.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "countries")
public class CountryEntity {
	
	@Id
	private String name;
	
	private String country;
	
	private String latitude;
	
	private String longitude;
	

	
}