package com.dws.challenge.web;

import com.dws.challenge.exception.IncompleteTransferDetailsException;
import com.dws.challenge.exception.SelfTransferException;
import com.dws.challenge.model.TransferCompletionDetails;
import com.dws.challenge.model.TransferMoneyDetails;
import com.dws.challenge.service.TransferManagementService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransferMoneyControllerTest {

    @Mock
    private TransferManagementService transferManagementService;

    private TransferMoneyController transferMoneyController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        transferMoneyController = new TransferMoneyController(transferManagementService);
    }

    @Test
    public void testTransferSuccessful() {
        // Prepare test data
        TransferMoneyDetails transferMoneyDetails = new TransferMoneyDetails();
        transferMoneyDetails.setFrom("account1");
        transferMoneyDetails.setTo("account2");
        transferMoneyDetails.setAmount(BigDecimal.valueOf(100.0));

        TransferCompletionDetails transferCompletionDetails = new TransferCompletionDetails();


        Mockito.when(transferManagementService.transfer(Mockito.any(TransferMoneyDetails.class)))
                .thenReturn(transferCompletionDetails);

        // Execute test
        ResponseEntity<TransferCompletionDetails> response = transferMoneyController.transfer(transferMoneyDetails);

        // Verify results
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(transferCompletionDetails, response.getBody());
        Mockito.verify(transferManagementService, Mockito.times(1))
                .transfer(Mockito.eq(transferMoneyDetails));

    }

    @Test
    public void testTransferIncompleteDetailsException() {
        // Prepare test data
        TransferMoneyDetails transferMoneyDetails = new TransferMoneyDetails();
        transferMoneyDetails.setFrom(null);
        transferMoneyDetails.setTo("account2");
        transferMoneyDetails.setAmount(BigDecimal.valueOf(100.0));

        // Execute test
        Assertions.assertThrows(IncompleteTransferDetailsException.class,
                () -> transferMoneyController.transfer(transferMoneyDetails));

        // Verify results
        Mockito.verify(transferManagementService, Mockito.never())
                .transfer(Mockito.any(TransferMoneyDetails.class));
    }

    @Test
    public void testTransferSelfTransferException() {
        // Prepare test data
        TransferMoneyDetails transferMoneyDetails = new TransferMoneyDetails();
        transferMoneyDetails.setFrom("account1");
        transferMoneyDetails.setTo("account1");
        transferMoneyDetails.setAmount(BigDecimal.valueOf(100.0));

        // Execute test
        Assertions.assertThrows(SelfTransferException.class,
                () -> transferMoneyController.transfer(transferMoneyDetails));

        // Verify results
        Mockito.verify(transferManagementService, Mockito.never())
                .transfer(Mockito.any(TransferMoneyDetails.class));
    }



}
