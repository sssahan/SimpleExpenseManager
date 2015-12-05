package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;

import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by sachithra sahan on 12/5/2015.
 */
public class PersisTransactionDAO implements TransactionDAO {
    DBHandler dbHandler=null;
    public PersisTransactionDAO(Context context){
        dbHandler=DBHandler.getInstance(context);
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {

    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        return null;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        return null;
    }
}
