package com.bms.account.management.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(schema = "bms", name = "account_type")
@Getter
@Setter
@DynamicUpdate
public class AccountType implements Serializable {

	private static final long serialVersionUID = -1193731224051796659L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long accountTypeId;

	@Column(length = 50,unique = true)
	private String type;

	private boolean status;

	@JsonIgnore
	private Date createdDate;

	@JsonIgnore
	private Date updatedDate;

}
