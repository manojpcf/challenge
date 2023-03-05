package com.dws.challenge.web;

import com.dws.challenge.domain.Account;
import com.dws.challenge.service.AccountsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/accounts")
@Api(value = "Accounts API", description = "Used for Adding new accounts and getting information of available Accounts")
@Slf4j
public class AccountsController {

  private final AccountsService accountsService;

  @Autowired
  public AccountsController(AccountsService accountsService) {
    this.accountsService = accountsService;
  }

  @ApiOperation(value = "Create an account", response = ResponseEntity.class)
  @ApiResponses(value = {
          @ApiResponse(code = 201, message = "Account created successfully"),
          @ApiResponse(code = 400, message = "Invalid input provided"),
          @ApiResponse(code = 500, message = "Internal server error occurred")
  })
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> createAccount(@RequestBody @Valid Account account) {
    log.info("Creating account {}", account);

    accountsService.createAccount(account);


    return new ResponseEntity<>(HttpStatus.CREATED);
  }
  @ApiOperation(value = "Get an account by account id", response = Account.class)
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Successfully retrieved the account"),
          @ApiResponse(code = 400, message = "Invalid input provided"),
          @ApiResponse(code = 404, message = "Account not found"),
          @ApiResponse(code = 500, message = "Internal server error occurred")
  })
  @GetMapping(path = "/{accountId}")
  public Account getAccount(@PathVariable String accountId) {
    log.info("Retrieving account for id {}", accountId);
    return this.accountsService.getAccount(accountId);
  }

}
