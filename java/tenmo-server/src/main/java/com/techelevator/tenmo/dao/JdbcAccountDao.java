package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


    @Service
    public class JdbcAccountDao implements AccountDao {

        @Autowired
        private JdbcTemplate jdbcTemplate;

        public JdbcAccountDao() {}

        public JdbcAccountDao(JdbcTemplate jdbcTemplate) {this.jdbcTemplate = jdbcTemplate;}

        @Override
        public BigDecimal getBalance (int userId) {
            String sql = "SELECT balance FROM accounts where user_id = ?";
            SqlRowSet results = null;
            BigDecimal balance = null;

            try {
                results = jdbcTemplate.queryForRowSet(sql, userId);
                if (results.next()) {
                    balance = results.getBigDecimal("balance");
                }
            } catch (DataAccessException e) {
                System.out.println("Error accessing data.");
            }
            return balance;
        }

        private Account mapToRowAccount(SqlRowSet result) {
            Account account = new Account();
            account.setBalance(result.getBigDecimal("balance"));
            account.setAccountId(result.getInt("account_id"));
            account.setUserId(result.getInt("user_id"));
            return account;
        }
    }
