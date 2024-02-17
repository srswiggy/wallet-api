package com.swiggy.wallet.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Money {
    private double amount;
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Money c)) {
            return false;
        }
        return Objects.equals(this.amount, ((Money) c).amount) && this.currency == ((Money) c).currency;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
