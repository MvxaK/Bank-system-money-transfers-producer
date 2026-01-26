package org.cook.bank_system.controller.apiController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cook.bank_system.model.TransferRequest;
import org.cook.bank_system.service.TransfersService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/transfers")
@Slf4j
public class TransferApiController {

    private final TransfersService transfersService;

    @PostMapping
    public ResponseEntity<Void> createTransfer(@RequestBody TransferRequest request){
        transfersService.transfer(request.getFromAccountId(), request.getToAccountId(), request.getAmount());

        log.info("Transfer created");

        return ResponseEntity.ok()
                .build();
    }

}
