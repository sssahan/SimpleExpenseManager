package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by sachithra sahan on 12/3/2015.
 */
public class DBHandler extends SQLiteOpenHelper {

    private static DBHandler db = null;
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "130525R";
    public static final String TABLE_ACCOUNT = "account";
    public static final String TABLE_TRANSACTION = "transaction";

    public static final String COLUMN_ID = "accountNo";
    public static final String COLUMN_BANKNAME = "bankName";
    public static final String COLUMN_ACHOLDERNAME = "accountHolderName";
    public static final String COLUMN_BALANCE="balance";

    public static final String COLUMN_ACCOUNT = "accountNo";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_EXPENCETYPE="expenseType";
    public static final String COLUMN_AMOUNT = "amount";

    private DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DBHandler getInstance(Context context){
        if(db==null){
            synchronized (DBHandler.class){
                db = new DBHandler(context);
            }
        }
        return db;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ACCOUNT_TABLE = "CREATE TABLE " +
                TABLE_ACCOUNT  + "("
                + COLUMN_ID + " TEXT PRIMARY KEY," + COLUMN_BANKNAME
                + " TEXT NOT NULL," + COLUMN_ACHOLDERNAME +"TEXT NOT NULL,"+ COLUMN_BALANCE+" INTEGER" + ")";
        db.execSQL(CREATE_ACCOUNT_TABLE);

        String CREATE_TABLE_TRANSACTION  = "CREATE TABLE " +
                TABLE_TRANSACTION   + "("
                + COLUMN_ACCOUNT+ " TEXT," +  COLUMN_DATE
                + " TEXT," + COLUMN_EXPENCETYPE +"REAL,"+ COLUMN_AMOUNT +" REAL" + ")";
        db.execSQL(CREATE_TABLE_TRANSACTION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNT);
        onCreate(db);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTION );
        onCreate(db);
    }

    public List<String> getAccountNumbersList() {
        List<String> accList=new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT "+COLUMN_ID + " FROM " +TABLE_ACCOUNT + " ;",null);
        while(res.isAfterLast() == false){
            accList.add(res.getString(res.getColumnIndex(COLUMN_ID)));
            res.moveToNext();
        }
        return accList;
    }

    public List<Account> getAccountsList() {
        List<Account> accList=new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT "+COLUMN_ID + " FROM " +TABLE_ACCOUNT + " ;",null);
        while(res.isAfterLast() == false){
            int idIndex= res.getColumnIndex(COLUMN_ID);
            int bankIndex=res.getColumnIndex(COLUMN_BANKNAME);
            int accHolderIndex=res.getColumnIndex(COLUMN_ACHOLDERNAME);
            int balanceIndex=res.getColumnIndex(COLUMN_BALANCE);

            Account account=new Account(res.getString(idIndex),res.getString(balanceIndex),res.getString(accHolderIndex),Double.valueOf(res.getString(balanceIndex)));
            accList.add(account);
            res.moveToNext();
        }
        return accList;
    }

    public Account getAccount(String accountNo){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT * FROM "+TABLE_ACCOUNT + " WHERE " +COLUMN_ID+"="+accountNo+ " ;",null);
        if(res.getCount()==0)
            return null;
        int idIndex= res.getColumnIndex(COLUMN_ID);
        int bankIndex=res.getColumnIndex(COLUMN_BANKNAME);
        int accHolderIndex=res.getColumnIndex(COLUMN_ACHOLDERNAME);
        int balanceIndex=res.getColumnIndex(COLUMN_BALANCE);

        Account account=new Account(res.getString(idIndex),res.getString(balanceIndex),res.getString(accHolderIndex),Double.valueOf(res.getString(balanceIndex)));
        return account;
    }

    public void addAccount(Account account) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID,account.getAccountNo());
        contentValues.put(COLUMN_BANKNAME,account.getBankName());
        contentValues.put(COLUMN_ACHOLDERNAME,account.getAccountHolderName());
        contentValues.put(COLUMN_BALANCE,account.getBalance());
    }

    public void removeAccount(String accountNo){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ACCOUNT, COLUMN_ID + " = " + accountNo, null);
    }

    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res =  db.rawQuery("SELECT "+COLUMN_BALANCE+" FROM " + TABLE_ACCOUNT + " WHERE " + COLUMN_ID + "=" + accountNo + " ;", null);
        if(res.getCount()==0)
            return;
        double balance=Double.valueOf(res.getString(res.getColumnIndex(COLUMN_BALANCE)));
        switch (expenseType){
            case EXPENSE:
                balance-=amount;
                break;
            case INCOME:
                balance+=amount;
                break;
        }
        db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COLUMN_BALANCE,balance);
        db.update(TABLE_ACCOUNT, contentValues, COLUMN_ID + " = " + accountNo, null);
    }

    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {

    }

    public List<Transaction> getAllTransactionLogs() {
        List<Transaction> transactionList=new ArrayList<>();
        /*SQLiteDatabase db = this.getWritableDatabase();
        Cursor res =  db.rawQuery("SELECT * FROM " + TABLE_TRANSACTION + " ;", null);
        while(res.isAfterLast() == false){
            int idIndex= res.getColumnIndex(COLUMN_ID);
            int bankIndex=res.getColumnIndex(COLUMN_BANKNAME);
            int accHolderIndex=res.getColumnIndex(COLUMN_ACHOLDERNAME);
            int balanceIndex=res.getColumnIndex(COLUMN_BALANCE);
        }*/
        return transactionList;
    }

    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List<Transaction> transactionList=new ArrayList<>();
        return transactionList;
    }

}
