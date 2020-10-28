package com.bms.account.management.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bms.account.management.domain.AccountCreationDTO;
import com.bms.account.management.domain.AccountTypesDTO;
import com.bms.account.management.domain.AccountUpdateDTO;
import com.bms.account.management.entities.Account;
import com.bms.account.management.entities.AccountType;
import com.bms.account.management.service.AccountService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("v1/account")
@Slf4j
public class AccountController {

	@Autowired
	AccountService accountService;

	@PostMapping("/create")
	public Account create(@RequestBody @Valid AccountCreationDTO accountDTO) {
		log.info("account creation request {}", accountDTO);
		return accountService.createAccount(accountDTO);
	}

	@PostMapping("/create-account-types")
	public List<AccountType> createAccountTypes(@RequestBody AccountTypesDTO typesDto) {
		log.info("account types creation request {}", typesDto);
		return accountService.createAccountTypes(typesDto);
	}

	@PutMapping("/credit")
	public Account credit(@RequestBody @Valid AccountUpdateDTO updateDTO) {
		log.info("account credit request {}", updateDTO);
		return accountService.credit(updateDTO);
	}

	@PutMapping("/debit")
	public Account debit(@RequestBody @Valid AccountUpdateDTO updateDTO) {
		log.info("account debit request {}", updateDTO);
		return accountService.debit(updateDTO);
	}

	@PutMapping("/enable")
	public Account enable(@RequestBody @Valid AccountUpdateDTO updateDTO) {
		log.info("account enable request {}", updateDTO);
		return accountService.toggleStatus(updateDTO, true);
	}

	@PutMapping("/disable")
	public Account disable(@RequestBody @Valid AccountUpdateDTO updateDTO) {
		log.info("account disable request {}", updateDTO);
		return accountService.toggleStatus(updateDTO, false);
	}
}
