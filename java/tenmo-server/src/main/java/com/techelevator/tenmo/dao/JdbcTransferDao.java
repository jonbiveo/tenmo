package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private AccountDao accountDao;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate;}

    @Override
    public List<Transfer> getAllTransfers(int userId) {
        List<Transfer> transferList = new ArrayList<>();
        String sql = "SELECT t.*, user1.username AS userFrom, user2.username AS userTo FROM transfers t " +
                "JOIN accounts a ON t.account_from = a.account_id " +
                "JOIN accounts b ON t.account_from = b.account_id " +
                "JOIN users user1 ON a.user_id = user1.user_id " +
                "JOIN users user2 ON a.user_id = user2.user_id " +
                "WHERE a.user_id = ? OR b.user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, userId);
        while (results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            transferList.add(transfer);
        }
        return transferList;
    }

    @Override
    public Transfer getTransferById(int transactionId) {
        Transfer transfer = new Transfer();
        String sql = "SELECT t.*, user1.username AS userFrom, user2.username AS userTo, ts.transfer_status_desc, tt.transfer_type_desc FROM transfers t " +
                "JOIN accounts a ON t.account_from = a.account_id " +
                "JOIN accounts b ON t.account_to = b.account_id " +
                "JOIN users user1 ON a.user_id = user1.user_id " +
                "JOIN users user2 ON b.user_id = user2.user_id " +
                "JOIN transfer_statuses ts ON t.transfer_status_id = ts.transfer_status_id " +
                "JOIN transfer_types tt ON t.transfer_type_id = tt.transfer_type_id " +
                "WHERE t.transfer_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transactionId);
        if (results.next()) {
            transfer = mapRowToTransfer(results);
        } else {
            throw new RuntimeException("Transfer not found.");
        }
        return transfer;
    }


    @Override
    public Transfer sendTransfer(int from, int to, BigDecimal amount) {
        String sql = "INSERT INTO transfers (transfer_type_id, transfer_status_id, account_from, account_to, amount) VALUES (2, 2, ?, ?, ?)";
        jdbcTemplate.update(sql, from, to, amount);
        String sql2 = "SELECT transfer_id FROM transfers ORDER BY transfer_id DESC LIMIT 1";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql2);
        int id = 0;
        while(result.next()) {
            id = result.getInt("transfer_id");
        }
        Transfer transfer = new Transfer(from, to, amount);
        return transfer;
    }


//    @Override
//    public String sendTransfer(int accountFrom, int accountTo, BigDecimal amount) {
//        accountFrom =
//        if (accountFrom == accountTo) {
//            return "You can not pocket your own money; it's already in your pocket!";
//        }
//        if (amount.compareTo(accountDao.getBalance(accountFrom)) == -1 && amount.compareTo(new BigDecimal(0)) == 1) {
//            String sql = "INSERT INTO transfers (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
//                    "VALUES (2, 2, ?, ?, ?);";
//            jdbcTemplate.update(sql, accountFrom, accountTo, amount);
//            accountDao.addToBalance(amount, accountTo);
//            accountDao.subtractFromBalance(amount, accountFrom);
//            return "Your transfer is complete.";
//        } else {
//            return "Failed transfer. Lack of funds, transfer amount less than or equal to zero, or invalid user.";
//        }
//    }

//    @Override
//    public String requestTransfer(int userFrom, int userTo, BigDecimal amount) {
//        if (userFrom == userTo) {
//            return "You can not pocket your own money; it's already in your pocket!";
//        }
//        if (amount.compareTo(new BigDecimal(0)) == 1) {
//            String sql = "INSERT INTO transfers (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
//                    "VALUES (1, 1, ?, ?, ?);";
//            jdbcTemplate.update(sql, userFrom, userTo, amount);
//            return "Your transfer request has been sent.";
//        } else {
//            return "Request not sent. There was a issue sending your request.";
//        }
//    }


    private Transfer mapRowToTransfer(SqlRowSet results) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(results.getInt("transfer_id"));
        transfer.setTransferTypeId(results.getInt("transfer_type_id"));
        transfer.setTransferStatusId(results.getInt("transfer_status_id"));
        transfer.setAccountFrom(results.getInt("account_From"));
        transfer.setAccountTo(results.getInt("account_to"));
        transfer.setAmount(results.getBigDecimal("amount"));
        try {
            transfer.setUserFrom(results.getString("userFrom"));
            transfer.setUserTo(results.getString("UserTo"));
        } catch (Exception e) {
            System.out.println("Can't set user from/to values.");
        }
        try {
            transfer.setTransferType(results.getString("transfer_type_desc"));
            transfer.setTransferStatus(results.getString("transfer_status_desc"));
        } catch (Exception e) {
            System.out.println("Can't set transfer type/status.");
        }
        return transfer;
    }
}
