package com.bms.account.management.domain;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AccountCreationDTO {

	@NotNull
	private Long accountTypeId;
	
	@NotNull
	private Long customerId;
	
	private Double minimumBalance;
}
