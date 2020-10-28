package com.bms.account.management.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bms.account.management.domain.AccountCreationDTO;
import com.bms.account.management.domain.AccountTypesDTO;
import com.bms.account.management.domain.AccountUpdateDTO;
import com.bms.account.management.entities.Account;
import com.bms.account.management.entities.AccountType;
import com.bms.account.management.entities.Customer;
import com.bms.account.management.exception.InvalidInputException;
import com.bms.account.management.exception.ResourceNotFoundException;
import com.bms.account.management.repository.AccountRepository;
import com.bms.account.management.repository.AccountTypeRepository;
import com.bms.account.management.repository.CustomerRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AccountService {

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	AccountTypeRepository accountTypeRepository;

	@Autowired
	CustomerRepository customerRepository;

	@Value("${response.invalid.customer.id}")
	private String invalidCustomerId;

	@Value("${response.invalid.account.type.id}")
	private String invalidAccountTypeId;

	public Account createAccount(AccountCreationDTO accountDTO) {

		Customer customer = getCustomer(accountDTO.getCustomerId());

		Optional<AccountType> accType = accountTypeRepository
				.findByAccountTypeIdAndStatusTrue(accountDTO.getAccountTypeId());

		if (!accType.isPresent()) {
			throw new InvalidInputException(invalidAccountTypeId);
		}

		Account account = new Account();
		account.setAccountType(accType.get());
		account.setCustomer(customer);
		account.setBalance(accountDTO.getMinimumBalance());
		account.setStatus(true);
		account.setInterestRate(interestRate(accType.get().getType()));
		Date date = new Date();
		account.setCreatedDate(date);
		account.setUpdatedDate(date);
		return accountRepository.save(account);
	}

	private Customer getCustomer(Long customerId) {
		Optional<Customer> customer = customerRepository.findById(customerId);

		if (!customer.isPresent()) {
			throw new InvalidInputException(invalidCustomerId);
		}
		return customer.get();
	}

	private Double interestRate(String accountType) {
		log.info("accountType - {}",accountType);
		// elaborate later
		return 6.0;
	}

	public List<AccountType> createAccountTypes(AccountTypesDTO typesDto) {
		Date date = new Date();
		List<AccountType> types = new ArrayList<AccountType>();
		typesDto.getTypes().forEach(type -> {
			AccountType acType = new AccountType();
			acType.setType(type);
			acType.setCreatedDate(date);
			acType.setUpdatedDate(date);
			acType.setStatus(true);
			types.add(acType);

		});
		if (!types.isEmpty()) {
			accountTypeRepository.saveAll(types);
		}
		return types;
	}

	public Account credit(AccountUpdateDTO updateDTO) {
		Account dbAccount = getValidAccount(updateDTO, true);
		dbAccount.setBalance(dbAccount.getBalance().doubleValue() + updateDTO.getAmount());

		return accountRepository.save(dbAccount);
	}

	private Account getValidAccount(AccountUpdateDTO updateDTO, boolean validateStatus) {
		Customer customer = getCustomer(updateDTO.getCustomerId());
		Optional<Account> account = accountRepository.findByAccountIdAndCustomer(updateDTO.getAccountId(), customer);

		if (!account.isPresent()) {
			throw new ResourceNotFoundException("Invalid input");
		}

		if (validateStatus && !account.get().isStatus()) {
			throw new InvalidInputException("Account is inactive");
		}

		Account dbAccount = account.get();
		return dbAccount;
	}

	public Account toggleStatus(AccountUpdateDTO updateDTO, boolean status) {
		Account dbAccount = getValidAccount(updateDTO, !status);
		dbAccount.setStatus(status);
		return accountRepository.save(dbAccount);
	}

	public Account debit(AccountUpdateDTO updateDTO) {
		Account dbAccount = getValidAccount(updateDTO, true);
		dbAccount.setBalance(dbAccount.getBalance().doubleValue() - updateDTO.getAmount());
		return accountRepository.save(dbAccount);
	}

}
