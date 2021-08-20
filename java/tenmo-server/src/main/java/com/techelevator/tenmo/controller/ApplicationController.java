package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class ApplicationController {

    @Autowired
    private AccountDao accountDao;
    @Autowired
    private UserDao userDao;


    public ApplicationController(AccountDao accountDao, UserDao userDao) {
        this.accountDao = accountDao;
        this.userDao = userDao;
    }

    @RequestMapping(path = "/balance/{id}", method = RequestMethod.GET)
    public BigDecimal getBalance(@PathVariable int id) {
        BigDecimal balance = accountDao.getBalance(id);
        return balance;
    }

    @RequestMapping(path = "listusers", method = RequestMethod.GET)
    public List<User> listUsers() {
        List<User> users = userDao.findAll();
        return users;
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @RequestMapping(path = "new-transfer/approved", method = RequestMethod.PUT)
    public void transferMoney (@RequestBody Transfer transfer) {
        accountDao.transferFunds(transfer);
    }

//    @RequestMapping(path = "users/{id}/balance", method = RequestMethod.PUT)
//    public void addToBalance(@Valid @RequestBody Transfer transfer, @PathVariable int id) {accountDao.addToBalance(transfer.getAmount(), id);}
//
//    @RequestMapping(path = "users/{id}/balance", method = RequestMethod.PUT)
//    public void subtractFromBalance (@Valid @RequestBody Transfer transfer, @PathVariable int id) {accountDao.subtractFromBalance(transfer.getAmount(), id);}

}
