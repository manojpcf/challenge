package com.dws.challenge.web;

import com.dws.challenge.model.TransferCompletionDetails;
import com.dws.challenge.model.TransferMoneyDetails;
import com.dws.challenge.service.TransferManagementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransferMoneyControllerConcurrencyTest {

    @Mock
    private TransferManagementService transferManagementService;

    @InjectMocks
    private TransferMoneyController transferMoneyController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testTransferConcurrency() throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 10; i++) {
            TransferMoneyDetails transferMoneyDetails = new TransferMoneyDetails();
            transferMoneyDetails.setFrom("from-account-" + i);
            transferMoneyDetails.setTo("to-account-" + i);
            transferMoneyDetails.setAmount(new BigDecimal(100.0));

            Runnable task = () -> {
                ResponseEntity<TransferCompletionDetails> responseEntity =
                        transferMoneyController.transfer(transferMoneyDetails);

                assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            };

            executorService.submit(task);
        }

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);
    }
}
