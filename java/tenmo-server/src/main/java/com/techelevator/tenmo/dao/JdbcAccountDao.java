package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Balance;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;


    @Component
    public class JdbcAccountDao implements AccountDao {
        private JdbcTemplate jdbcTemplate;

        public JdbcAccountDao() {this.jdbcTemplate = new JdbcTemplate();}

        @Override
        public Balance getBalance (String user) {

            Balance balance = new Balance();
            balance.setBalance(new BigDecimal("200"));

            return balance;
        }
    }
