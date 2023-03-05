package com.dws.challenge.web;

import com.dws.challenge.exception.IncompleteTransferDetailsException;
import com.dws.challenge.exception.SelfTransferException;
import com.dws.challenge.model.TransferCompletionDetails;
import com.dws.challenge.model.TransferMoneyDetails;
import com.dws.challenge.service.TransferManagementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;




@RestController
@RequestMapping("/v1/transfers")
@Api(value = "Transfer Money API", produces = MediaType.APPLICATION_JSON_VALUE)
public class TransferMoneyController {

    private final TransferManagementService transferManagementService;
    @Autowired
    public TransferMoneyController(TransferManagementService transferManagementService) {
        this.transferManagementService = transferManagementService;
    }
    @ApiOperation(value = "Transfer money between two accounts", response = TransferCompletionDetails.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully transferred the money"),
            @ApiResponse(code = 400, message = "Invalid or incomplete transfer details provided"),
            @ApiResponse(code = 500, message = "Internal server error occurred")
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TransferCompletionDetails> transfer(@RequestBody TransferMoneyDetails transferMoneyDetails) {
        validate(transferMoneyDetails);

        return  ResponseEntity.status(HttpStatus.OK).body(transferManagementService.transfer(transferMoneyDetails));
    }

    private void validate(TransferMoneyDetails transferMoneyDetails) {
        if (transferMoneyDetails.getFrom() == null) {
            throw new IncompleteTransferDetailsException("Missing required attribute 'from account id'");
        }
        if (transferMoneyDetails.getTo() == null) {
            throw new IncompleteTransferDetailsException("Missing required attribute 'to account id'");
        }
        if (transferMoneyDetails.getAmount() == null) {
            throw new IncompleteTransferDetailsException("Missing required attribute 'transfer amount'");
        }
        if (transferMoneyDetails.getTo().equals(transferMoneyDetails.getFrom())) {
            throw new SelfTransferException("From accountId and To accountId can't be same");
        }
    }
}
