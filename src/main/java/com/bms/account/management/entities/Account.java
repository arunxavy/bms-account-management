package com.bms.account.management.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(schema = "bms", name = "account")
@Getter
@Setter
@DynamicUpdate
public class Account implements Serializable {

	private static final long serialVersionUID = -1193731224051796659L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long accountId;

	@JsonManagedReference
	@ManyToOne
	@JoinColumn(name = "customer_id", nullable = false)
	private Customer customer;

	@JsonManagedReference
	@ManyToOne
	@JoinColumn(name = "account_type_id", nullable = false)
	private AccountType accountType;

	private Double balance;

	private Double interestRate;

	private boolean status;

	@JsonIgnore
	private Date createdDate;

	@JsonIgnore
	private Date updatedDate;

}
