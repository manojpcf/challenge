package com.dws.challenge.service;

import com.dws.challenge.domain.Account;
import com.dws.challenge.model.TransferCompletionDetails;
import com.dws.challenge.model.TransferMoneyDetails;
import com.dws.challenge.repository.AccountResourceDataProvider;
import com.dws.challenge.repository.AccountsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

class TransferManagementServiceTest {

    @Mock
    private AccountsRepository accountsRepository;

    @Mock
    private AccountResourceDataProvider resourceDataProvider;

    @Mock
    private EmailNotificationService notificationService;

    private TransferManagementService transferManagementService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        transferManagementService = new TransferManagementService(accountsRepository, resourceDataProvider, notificationService);
    }

    @Test
    void transfer_shouldLockAccountsInOrderToPreventDeadlocks() {
        String fromAccountId = "123";
        String toAccountId = "456";
        BigDecimal amount = BigDecimal.TEN;

        TransferMoneyDetails transferMoneyDetails = new TransferMoneyDetails();
        transferMoneyDetails.setFrom(fromAccountId);
        transferMoneyDetails.setTo(toAccountId);
        transferMoneyDetails.setAmount(amount);

        Account fromAccount = new Account(fromAccountId,BigDecimal.valueOf(100));
        Account toAccount = new Account(toAccountId,BigDecimal.valueOf(50));

        when(accountsRepository.getAccount(fromAccountId)).thenReturn(fromAccount);
        when(accountsRepository.getAccount(toAccountId)).thenReturn(toAccount);

        Thread t1 = new Thread(() -> transferManagementService.transfer(transferMoneyDetails));
        TransferMoneyDetails transferMoneyDetailsThread = new TransferMoneyDetails();
        transferMoneyDetailsThread.setFrom(fromAccountId);
        transferMoneyDetailsThread.setTo(toAccountId);
        transferMoneyDetailsThread.setAmount(amount);
        Thread t2 = new Thread(() -> transferManagementService.transfer(transferMoneyDetailsThread));

        // Start the threads to trigger the transfer
        t1.start();
        t2.start();

        // Wait for the threads to finish
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Verify that the accounts were locked in order to prevent deadlocks
        verify(resourceDataProvider, atLeastOnce()).debit(fromAccountId, amount);
        verify(resourceDataProvider, atLeastOnce()).credit(toAccountId, amount);
    }
    @Test
    public void testTransfer_SuccessfulTransfer() {
        TransferMoneyDetails transferMoneyDetails = new TransferMoneyDetails();
        transferMoneyDetails.setFrom("123");
        transferMoneyDetails.setTo("456");
        transferMoneyDetails.setAmount(new BigDecimal(50));

        TransferCompletionDetails expected = new TransferCompletionDetails();
        expected.setFrom("123");
        expected.setFromBalance(new BigDecimal(450));
        expected.setTo("456");
        expected.setToBalance(new BigDecimal(550));

        // mock behavior of repository and resourceDataProvider
        when(accountsRepository.getAccount("123")).thenReturn(new Account("123", new BigDecimal(500)));
        when(accountsRepository.getAccount("456")).thenReturn(new Account("456", new BigDecimal(500)));

        doNothing().when(resourceDataProvider).debit("123", new BigDecimal(50));
        doNothing().when(resourceDataProvider).credit("456", new BigDecimal(50));

        TransferCompletionDetails actual = transferManagementService.transfer(transferMoneyDetails);

        verify(accountsRepository, times(1)).getAccount("123");
        verify(accountsRepository, times(1)).getAccount("456");
        verify(resourceDataProvider, times(1)).debit("123", new BigDecimal(50));
        verify(resourceDataProvider, times(1)).credit("456", new BigDecimal(50));
        verify(notificationService, times(2)).notifyAboutTransfer(any(Account.class), anyString());
    }



}
