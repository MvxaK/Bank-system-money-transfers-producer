package org.cook.bank_system.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferRequest {

    private Long fromAccountId;
    private Long toAccountId;
    private BigDecimal amount;

}
