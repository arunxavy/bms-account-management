package com.bms.account.management.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(schema = "bms", name = "customer")
@Getter
@Setter
@DynamicUpdate
public class Customer implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5142284415095785546L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long customerId;

	private String pan;

	private String username;

	private String firstName;

	private String lastName;

	private String gender;

	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date dob;

	private String email;

	private String mobileNumber;

	private String addressLineOne;

	private String addressLineTwo;

	private String pincode;

	private String city;

	private String state;

	private String country;

	private String nationality;

	private boolean enabled;

	@JsonBackReference
	@OneToMany(mappedBy = "customer")
	private Set<Account> accounts;

	@JsonIgnore
	private Date createdDate;

	@JsonIgnore
	private Date updatedDate;

	public Customer() { // default empty constructor
	}

}
