package com.dws.challenge.web;

import com.dws.challenge.domain.Account;
import com.dws.challenge.exception.DuplicateAccountIdException;
import com.dws.challenge.service.AccountsService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountsController.class)
public class AccountsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountsService accountsService;

    @Test
    public void testCreateAccount() throws Exception {
        Account account = new Account("1234", BigDecimal.TEN);
        String accountJson = "{\"accountId\": \"1234\", \"balance\": 10}";

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(accountJson))
                .andExpect(status().isCreated());

        // Verify that the service method was called with the correct argument
        Mockito.verify(accountsService).createAccount(account);
    }


    @Test
    public void testCreateAccountWithInvalidInput() throws Exception {
        // Test with an account object that has a negative initial balance
        Account account = new Account("1234", BigDecimal.valueOf(-10));
        String accountJson = "{\"accountId\": \"1234\", \"balance\": -10}";

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(accountJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateAccountWithDuplicateAccountId() throws Exception {
        // Set up the mock service to throw a DuplicateAccountIdException
        doThrow(DuplicateAccountIdException.class).when(accountsService).createAccount(any(Account.class));

        Account account = new Account("1234", BigDecimal.TEN);
        String accountJson = "{\"accountId\": \"1234\", \"balance\": 10}";

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(accountJson))
                .andExpect(status().isConflict());
    }

    @Test
    public void testGetAccount() throws Exception {
        Account account = new Account("1234", BigDecimal.TEN);
        when(accountsService.getAccount("1234")).thenReturn(account);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/accounts/1234"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"accountId\": \"1234\", \"balance\": 10}"));
    }



}
