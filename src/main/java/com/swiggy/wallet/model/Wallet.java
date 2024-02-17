package com.swiggy.wallet.model;

import com.swiggy.wallet.exceptions.InsufficientFundsException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name="wallet")
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Embedded
    private Money money;

    public Wallet(Currency currency) {
        this.money = new Money(0.0, currency);
    }

    public void credit(Money money) {
        this.money =  new Money(this.money.getAmount() + money.getAmount(), this.money.getCurrency());
    }

    public void debit(Money money) throws InsufficientFundsException {
        if((this.money.getAmount() - money.getAmount()) < 0) throw new InsufficientFundsException();
        this.money = new Money(this.money.getAmount() - money.getAmount(), this.money.getCurrency());
    }
}
