package com.Banking.accounts.service;


import com.Banking.accounts.Repository.AccountRepository;
import com.Banking.accounts.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class AccountService {


    @Autowired
    private AccountRepository accountRepository;

    public Account getAccount(String accountNumber) {
        List<Account> accounts = accountRepository.findByAccountNumber(accountNumber);
        if (accounts.isEmpty()) {
            throw new NoSuchElementException("No account found with account number: " + accountNumber);
        }
        if (accounts.size() > 1) {
            // Log a warning about duplicate accounts
            System.out.println("Warning: Multiple accounts found with account number: " + accountNumber);
        }
        // Return the first account found
        return accounts.get(0);
    }

    public Account createAccount(Account account) {
        // Check if an account with this number already exists
        List<Account> existingAccounts = accountRepository.findByAccountNumber(account.getAccountNumber());
        if (!existingAccounts.isEmpty()) {
            throw new IllegalArgumentException("An account with this number already exists");
        }
        return accountRepository.save(account);
    }

    public Account deposit(String accountNumber, BigDecimal amount) {
        Account account = getAccount(accountNumber);
        account.setBalance(account.getBalance().add(amount));
        return accountRepository.save(account);
    }

    public Account withdraw(String accountNumber, BigDecimal amount) {
        Account account = getAccount(accountNumber);
        if (account.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        account.setBalance(account.getBalance().subtract(amount));
        return accountRepository.save(account);
    }

}