package com.bms.account.management.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bms.account.management.entities.Account;
import com.bms.account.management.entities.Customer;

public interface AccountRepository extends JpaRepository<Account, Long>{

	Optional<Account> findByAccountIdAndCustomer(Long accountId,Customer customer);

}
