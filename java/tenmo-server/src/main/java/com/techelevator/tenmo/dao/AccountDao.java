package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public interface AccountDao {

    BigDecimal getBalance (int userId);
    //BigDecimal addToBalance(BigDecimal amountToAdd, int id);
    //BigDecimal subtractFromBalance(BigDecimal amountToSubtract, int id);
    Account findUserById(int userId);
    public Account findAccountById(int id);
    void transferFunds (Transfer transfer);

}
