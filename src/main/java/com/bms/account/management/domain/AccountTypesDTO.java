package com.bms.account.management.domain;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class AccountTypesDTO {

	@NotEmpty
	private List<String> types;
}
