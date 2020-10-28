package com.bms.account.management;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.ResourceAccessException;

import com.bms.account.management.domain.AccountCreationDTO;
import com.bms.account.management.domain.AccountTypesDTO;
import com.bms.account.management.domain.AccountUpdateDTO;
import com.bms.account.management.entities.Account;
import com.bms.account.management.entities.AccountType;
import com.bms.account.management.entities.Customer;
import com.bms.account.management.repository.AccountRepository;
import com.bms.account.management.repository.AccountTypeRepository;
import com.bms.account.management.repository.CustomerRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, properties = { "server.port:0",
		"spring.cloud.config.discovery.enabled:false", "spring.cloud.config.enabled:false",
		"spring.profiles.active:test" })
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@TestPropertySource("classpath:account-management-junit.properties")
class AccountManagementApplicationTests {

	private static final String ACCOUNT_TYPE_CREATION_URL = "/v1/account/create-account-types";
	private static final String ACCOUNT_CREATION_URL = "/v1/account/create";

	private static final String ACCOUNT_CREDIT_URL = "/v1/account/credit";

	private static final String ACCOUNT_DEBIT_URL = "/v1/account/debit";

	private static final String ACCOUNT_ENABLE_URL = "/v1/account/enable";

	private static final String ACCOUNT_DISABLE_URL = "/v1/account/disable";
	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	CustomerRepository customerRepository;

	@MockBean
	AccountRepository accountRepository;

	@MockBean
	AccountTypeRepository accountTypeRepository;

	@Test
	public void testCreateAccountTypes() throws JsonProcessingException, Exception {

		
		
		Customer c = new Customer();
		c.setAddressLineOne("324");
		c.setAddressLineTwo("sdfsd");
		c.setCity("kochi");
		c.setCountry("ind");
		c.setCreatedDate(new Date());
		c.setDob(new Date());
		c.setEmail("mail");
		assertNotNull(c);
		AccountTypesDTO atDto = new AccountTypesDTO();
		List<String> types = new ArrayList<>();
		types.add("housing");
		types.add("vehicle");
		atDto.setTypes(types);
		String jsonReq = objectMapper.writeValueAsString(atDto);
		doReturn(Collections.emptyList()).when(accountTypeRepository).saveAll(Mockito.any());
		mockMvc.perform(post(ACCOUNT_TYPE_CREATION_URL).contentType("application/json").content(jsonReq))
				.andExpect(status().isOk());
	}

	@Test
	public void testCreateAccount() throws JsonProcessingException, Exception {

		AccountCreationDTO acDto = new AccountCreationDTO();
		acDto.setAccountTypeId(Long.valueOf(10));
		acDto.setCustomerId(Long.valueOf(100));
		acDto.setMinimumBalance(Double.valueOf("1500.00"));
		String jsonReq = objectMapper.writeValueAsString(acDto);

		Optional<Customer> customer = Optional.of(new Customer());
		doReturn(customer).when(customerRepository).findById(Mockito.any());

		Optional<AccountType> accountType = Optional.of(new AccountType());
		doReturn(accountType).when(accountTypeRepository).findByAccountTypeIdAndStatusTrue(Mockito.anyLong());

		doReturn(new Account()).when(accountRepository).save(Mockito.any());

		mockMvc.perform(post(ACCOUNT_CREATION_URL).contentType("application/json").content(jsonReq))
				.andExpect(status().isOk());
	}

	@Test
	public void testCreateAccountInvalidType() throws JsonProcessingException, Exception {

		AccountCreationDTO acDto = new AccountCreationDTO();
		acDto.setAccountTypeId(Long.valueOf(10));
		acDto.setCustomerId(Long.valueOf(100));
		acDto.setMinimumBalance(Double.valueOf("1500.00"));
		String jsonReq = objectMapper.writeValueAsString(acDto);

		Optional<Customer> customer = Optional.of(new Customer());
		doReturn(customer).when(customerRepository).findById(Mockito.any());

		Optional<AccountType> accountType = Optional.empty();
		doReturn(accountType).when(accountTypeRepository).findByAccountTypeIdAndStatusTrue(Mockito.anyLong());

		doReturn(new Account()).when(accountRepository).save(Mockito.any());

		mockMvc.perform(post(ACCOUNT_CREATION_URL).contentType("application/json").content(jsonReq))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void testCreateAccountInvalidCustomer() throws JsonProcessingException, Exception {

		AccountCreationDTO acDto = new AccountCreationDTO();
		acDto.setAccountTypeId(Long.valueOf(10));
		acDto.setCustomerId(Long.valueOf(100));
		acDto.setMinimumBalance(Double.valueOf("1500.00"));
		String jsonReq = objectMapper.writeValueAsString(acDto);

		Optional<Customer> customer = Optional.empty();
		doReturn(customer).when(customerRepository).findById(Mockito.any());

		Optional<AccountType> accountType = Optional.empty();
		doReturn(accountType).when(accountTypeRepository).findByAccountTypeIdAndStatusTrue(Mockito.anyLong());

		doReturn(new Account()).when(accountRepository).save(Mockito.any());

		mockMvc.perform(post(ACCOUNT_CREATION_URL).contentType("application/json").content(jsonReq))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void testCreateAccountNoCustomerId() throws JsonProcessingException, Exception {

		AccountCreationDTO acDto = new AccountCreationDTO();
		acDto.setAccountTypeId(Long.valueOf(10));
		acDto.setMinimumBalance(Double.valueOf("1500.00"));
		String jsonReq = objectMapper.writeValueAsString(acDto);

		mockMvc.perform(post(ACCOUNT_CREATION_URL).contentType("application/json").content(jsonReq))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void testCredit() throws JsonProcessingException, Exception {

		AccountUpdateDTO acDto = new AccountUpdateDTO();
		acDto.setCustomerId(Long.valueOf(10));
		acDto.setAmount(Double.valueOf("1500.00"));
		acDto.setAccountId(Long.valueOf(1));
		String jsonReq = objectMapper.writeValueAsString(acDto);
		Optional<Account> account = validMockAccount();
		doReturn(account).when(accountRepository).findByAccountIdAndCustomer(Mockito.anyLong(), Mockito.any());
		Optional<Customer> customer = Optional.of(new Customer());
		doReturn(customer).when(customerRepository).findById(Mockito.any());
		doReturn(new Account()).when(accountRepository).save(Mockito.any());
		mockMvc.perform(put(ACCOUNT_CREDIT_URL).contentType("application/json").content(jsonReq))
				.andExpect(status().isOk());
	}

	private Optional<Account> validMockAccount() {
		Account dbAccount = new Account();
		dbAccount.setStatus(true);
		dbAccount.setBalance(Double.valueOf("1500.00"));
		Optional<Account> account = Optional.of(dbAccount);
		return account;
	}

	@Test
	public void testCreditInvalidAccountId() throws JsonProcessingException, Exception {

		AccountUpdateDTO acDto = new AccountUpdateDTO();
		acDto.setCustomerId(Long.valueOf(10));
		acDto.setAmount(Double.valueOf("1500.00"));
		acDto.setAccountId(Long.valueOf(1));
		String jsonReq = objectMapper.writeValueAsString(acDto);
		Optional<Account> account = Optional.empty();
		Optional<Customer> customer = Optional.of(new Customer());
		doReturn(customer).when(customerRepository).findById(Mockito.any());
		doReturn(account).when(accountRepository).findByAccountIdAndCustomer(Mockito.anyLong(), Mockito.any());

		mockMvc.perform(put(ACCOUNT_CREDIT_URL).contentType("application/json").content(jsonReq))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testCreditInvactiveAccountId() throws JsonProcessingException, Exception {

		AccountUpdateDTO acDto = new AccountUpdateDTO();
		acDto.setCustomerId(Long.valueOf(10));
		acDto.setAmount(Double.valueOf("1500.00"));
		acDto.setAccountId(Long.valueOf(1));
		String jsonReq = objectMapper.writeValueAsString(acDto);
		Account dbAccount = new Account();
		dbAccount.setStatus(false);
		dbAccount.setBalance(Double.valueOf("1500.00"));
		Optional<Account> account = Optional.of(dbAccount);
		Optional<Customer> customer = Optional.of(new Customer());
		doReturn(customer).when(customerRepository).findById(Mockito.any());
		doReturn(account).when(accountRepository).findByAccountIdAndCustomer(Mockito.anyLong(), Mockito.any());

		mockMvc.perform(put(ACCOUNT_CREDIT_URL).contentType("application/json").content(jsonReq))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	public void testServerError() throws JsonProcessingException, Exception {

		AccountUpdateDTO acDto = new AccountUpdateDTO();
		acDto.setCustomerId(Long.valueOf(10));
		acDto.setAmount(Double.valueOf("1500.00"));
		acDto.setAccountId(Long.valueOf(1));
		String jsonReq = objectMapper.writeValueAsString(acDto);
		
		doThrow(new ResourceAccessException("error")).when(customerRepository).findById(Mockito.any());

		mockMvc.perform(put(ACCOUNT_CREDIT_URL).contentType("application/json").content(jsonReq))
				.andExpect(status().is5xxServerError());
	}

	@Test
	public void testDebit() throws JsonProcessingException, Exception {

		AccountUpdateDTO acDto = new AccountUpdateDTO();
		acDto.setCustomerId(Long.valueOf(10));
		acDto.setAmount(Double.valueOf("1500.00"));
		acDto.setAccountId(Long.valueOf(1));
		String jsonReq = objectMapper.writeValueAsString(acDto);
		Optional<Account> account = validMockAccount();
		doReturn(account).when(accountRepository).findByAccountIdAndCustomer(Mockito.anyLong(), Mockito.any());
		Optional<Customer> customer = Optional.of(new Customer());
		doReturn(customer).when(customerRepository).findById(Mockito.any());
		doReturn(new Account()).when(accountRepository).save(Mockito.any());
		mockMvc.perform(put(ACCOUNT_DEBIT_URL).contentType("application/json").content(jsonReq))
				.andExpect(status().isOk());
	}

	@Test
	public void testEnable() throws JsonProcessingException, Exception {

		AccountUpdateDTO acDto = new AccountUpdateDTO();
		acDto.setCustomerId(Long.valueOf(10));
		acDto.setAmount(Double.valueOf("1500.00"));
		acDto.setAccountId(Long.valueOf(1));
		String jsonReq = objectMapper.writeValueAsString(acDto);
		Optional<Account> account = validMockAccount();
		doReturn(account).when(accountRepository).findByAccountIdAndCustomer(Mockito.anyLong(), Mockito.any());
		Optional<Customer> customer = Optional.of(new Customer());
		doReturn(customer).when(customerRepository).findById(Mockito.any());
		doReturn(new Account()).when(accountRepository).save(Mockito.any());
		mockMvc.perform(put(ACCOUNT_ENABLE_URL).contentType("application/json").content(jsonReq))
				.andExpect(status().isOk());
	}

	@Test
	public void testDisable() throws JsonProcessingException, Exception {

		AccountUpdateDTO acDto = new AccountUpdateDTO();
		acDto.setCustomerId(Long.valueOf(10));
		acDto.setAmount(Double.valueOf("1500.00"));
		acDto.setAccountId(Long.valueOf(1));
		String jsonReq = objectMapper.writeValueAsString(acDto);
		Optional<Account> account = validMockAccount();
		doReturn(account).when(accountRepository).findByAccountIdAndCustomer(Mockito.anyLong(), Mockito.any());
		Optional<Customer> customer = Optional.of(new Customer());
		doReturn(customer).when(customerRepository).findById(Mockito.any());
		doReturn(new Account()).when(accountRepository).save(Mockito.any());
		mockMvc.perform(put(ACCOUNT_DISABLE_URL).contentType("application/json").content(jsonReq))
				.andExpect(status().isOk());
	}

}
