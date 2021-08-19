package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {

    public List<Transfer> getAllTransfers(int userId);
    public Transfer getTransferById(int transactionId);
}
