package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;

import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

/**
 * Created by sachithra sahan on 12/3/2015.
 */
public class PersistentAccountDAO implements AccountDAO{
    DBHandler dbHandler=null;
    public PersistentAccountDAO(DBHandler dbHandler){
        this.dbHandler=dbHandler;
    }
    @Override
    public List<String> getAccountNumbersList() {
        return dbHandler.getAccountNumbersList();
    }

    @Override
    public List<Account> getAccountsList() {
        return dbHandler.getAccountsList();
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Account account = dbHandler.getAccount(accountNo);
        if (account==null){
            String msg = "The accountNo" + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
        return account;

    }

    @Override
    public void addAccount(Account account) {
        dbHandler.addAccount(account);
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        if (!dbHandler.removeAccount(accountNo)){
            String msg = "The accountNo "+ accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }

    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        Account account = getAccount(accountNo);
        switch (expenseType){
            case EXPENSE:
                account.setBalance(account.getBalance()-amount);
                break;
            case INCOME:
                account.setBalance(account.getBalance()+amount);
                break;
        }
        dbHandler.updateBalance(account);
    }

}

