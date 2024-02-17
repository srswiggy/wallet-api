package com.swiggy.wallet.requestmodels;

import com.swiggy.wallet.model.Money;
import jakarta.persistence.Embedded;
import lombok.Data;

@Data
public class UserTransferMoneyRequest {
    private String receiverUsername;
    @Embedded
    private Money money;
}
