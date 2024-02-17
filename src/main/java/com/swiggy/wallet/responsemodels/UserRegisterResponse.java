package com.swiggy.wallet.responsemodels;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class UserRegisterResponse {
    private String message = "User Created Successfully";
}
