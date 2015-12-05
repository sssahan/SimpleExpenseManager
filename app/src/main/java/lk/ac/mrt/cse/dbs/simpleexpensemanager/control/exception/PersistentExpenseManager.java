package lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception;

import android.content.Context;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.ExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.DBHandler;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersisTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;

/**
 * Created by sachithra sahan on 12/3/2015.
 */
public class PersistentExpenseManager extends ExpenseManager{
    private Context context = null;
    public PersistentExpenseManager(Context context) {
        this.context = context;
        setup();
    }

    @Override
    public void setup() {
        DBHandler dbHandler=DBHandler.getInstance(context);

        TransactionDAO persisTransactionDAO = new PersisTransactionDAO(dbHandler);
        setTransactionsDAO(persisTransactionDAO);

        AccountDAO persistentAccountDAO = new PersistentAccountDAO(dbHandler);
        setAccountsDAO(persistentAccountDAO);

        getAccountsDAO().addAccount(new Account("12345A", "Yoda Bank", "Anakin Skywalker", 10000.0));
        getAccountsDAO().addAccount(new Account("78945Z", "Clone BC", "Obi-Wan Kenobi", 80000.0));
    }
}
