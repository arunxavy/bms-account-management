package com.bms.account.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bms.account.management.entities.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
