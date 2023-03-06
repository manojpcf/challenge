package com.dws.challenge.service;

import com.dws.challenge.domain.Account;
import com.dws.challenge.model.TransferCompletionDetails;
import com.dws.challenge.model.TransferMoneyDetails;
import com.dws.challenge.repository.AccountResourceDataProvider;
import com.dws.challenge.repository.AccountsRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransferManagementService {


    @Getter
    private final AccountsRepository accountsRepository;

    @Getter
    private final AccountResourceDataProvider resourceDataProvider;

    @Getter
    private final EmailNotificationService notificationService;

    @Autowired
    TransferManagementService(AccountsRepository accountsRepository, AccountResourceDataProvider resourceDataProvider, EmailNotificationService notificationService) {
        this.accountsRepository = accountsRepository;

        this.resourceDataProvider = resourceDataProvider;
        this.notificationService = notificationService;
    }




    public TransferCompletionDetails transfer(TransferMoneyDetails transferMoneyDetails) {

        Account fromAccount = accountsRepository.getAccount(transferMoneyDetails.getFrom());
        Account toAccount = accountsRepository.getAccount(transferMoneyDetails.getTo());

        /* construct the transferCompletionDetails outside
            of the synchronized blocks for minimalistic locking.
         */
        TransferCompletionDetails transferCompletionDetails = new TransferCompletionDetails();
        transferCompletionDetails.setFrom(fromAccount.getAccountId());
        transferCompletionDetails.setTo(toAccount.getAccountId());

        /*
            compare the accountIds of fromAccount and toAccount to determine the order
            of locking. This ensures that there's an order in which the objects are locked
            so that the "Cyclic Wait" condition never occurs. The account with lower id has
            higher priority and would need to be locked first. The "from" account and "to" account
            can't have same id as in this case the validate method throws SelfTransferException.
         */
        if (transferMoneyDetails.getFrom().compareTo(transferMoneyDetails.getTo()) > 0) {
            synchronized (fromAccount) {
                synchronized (toAccount) {
                    doTransfer(fromAccount,toAccount,transferCompletionDetails,transferMoneyDetails);
                }
            }
        } else {
            synchronized (toAccount) {
                synchronized (fromAccount) {
                    doTransfer(fromAccount,toAccount,transferCompletionDetails,transferMoneyDetails);
                }
            }
        }



        return transferCompletionDetails;
    }

    public void doTransfer(Account fromAccount, Account toAccount,TransferCompletionDetails transferCompletionDetails,TransferMoneyDetails transferMoneyDetails ){
        resourceDataProvider.debit(fromAccount.getAccountId(), transferMoneyDetails.getAmount());
        resourceDataProvider.credit(toAccount.getAccountId(), transferMoneyDetails.getAmount());
        transferCompletionDetails.setFromBalance(fromAccount.getBalance());
        transferCompletionDetails.setToBalance(toAccount.getBalance());
        notificationService.notifyAboutTransfer(fromAccount,"Transfer of " + transferMoneyDetails.getAmount() + " to account " + toAccount.getAccountId());
        notificationService.notifyAboutTransfer(toAccount, "Transfer of " + transferMoneyDetails.getAmount() + " from account " + fromAccount.getAccountId());
    }

}
