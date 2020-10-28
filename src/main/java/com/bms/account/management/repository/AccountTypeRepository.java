package com.bms.account.management.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bms.account.management.entities.AccountType;

public interface AccountTypeRepository extends JpaRepository<AccountType, Long>{

	Optional<AccountType> findByAccountTypeIdAndStatusTrue(Long accountTypeId);

}
