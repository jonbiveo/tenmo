package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {

    public List<Transfer> getAllTransfers(int userId);
    public Transfer getTransferById(int transactionId);
    public String sendTransfer(int userFrom, int UserTo, BigDecimal amount);

}
