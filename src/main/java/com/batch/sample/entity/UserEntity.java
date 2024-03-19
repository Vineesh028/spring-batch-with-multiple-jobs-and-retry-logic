package com.batch.sample.entity;


import javax.persistence.Column;
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
@Table(name = "userdetails")
public class UserEntity {

	@Id
	private String index;
	
	private String userId;
	
	private String firstName;
	
	private String lastName;
	
	private String sex;
	
	private String email;
	
	
	private String phone;
	
	@Column(name = "dob")
	private String dob;
	
	private String jobTitle;

}