package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    public static final String COLUMN_ACCOUNT_ID = "accountNo";
    public static final String COLUMN_BANK_NAME = "bankName";
    public static final String COLUMN_AC_HOLDER_NAME = "accountHolderName";
    public static final String COLUMN_BALANCE="balance";

    public static final String COLUMN_ACCOUNT_NO = "accountNo";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_EXPENCE_TYPE="expenseType";
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
                + COLUMN_ACCOUNT_ID + " TEXT PRIMARY KEY," + COLUMN_BANK_NAME
                + " TEXT NOT NULL," + COLUMN_AC_HOLDER_NAME +" TEXT NOT NULL,"+ COLUMN_BALANCE+" INTEGER" + ")";
        db.execSQL(CREATE_ACCOUNT_TABLE);

        String CREATE_TABLE_TRANSACTION  = "CREATE TABLE " +
                TABLE_TRANSACTION   + "("
                + COLUMN_ACCOUNT_NO+ " TEXT," +  COLUMN_DATE
                + " TEXT," + COLUMN_EXPENCE_TYPE +" REAL,"+ COLUMN_AMOUNT +" REAL" + ")";
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
        Cursor res =  db.rawQuery( "SELECT "+COLUMN_ACCOUNT_ID + " FROM " +TABLE_ACCOUNT + " ;",null);
        while(!res.isAfterLast()){
            accList.add(res.getString(res.getColumnIndex(COLUMN_ACCOUNT_ID)));
            res.moveToNext();
        }
        db.close();
        return accList;
    }

    public List<Account> getAccountsList() {
        List<Account> accList=new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("SELECT " + COLUMN_ACCOUNT_ID + " FROM " + TABLE_ACCOUNT + " ;", null);
        while(!res.isAfterLast()){
            int idIndex= res.getColumnIndex(COLUMN_ACCOUNT_ID);
            int bankIndex=res.getColumnIndex(COLUMN_BANK_NAME);
            int accHolderIndex=res.getColumnIndex(COLUMN_AC_HOLDER_NAME);
            int balanceIndex=res.getColumnIndex(COLUMN_BALANCE);

            Account account=new Account(res.getString(idIndex),res.getString(bankIndex),res.getString(accHolderIndex),Double.valueOf(res.getString(balanceIndex)));
            accList.add(account);
            res.moveToNext();
        }
        db.close();
        return accList;
    }

    public Account getAccount(String accountNo){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT * FROM "+TABLE_ACCOUNT + " WHERE " +COLUMN_ACCOUNT_ID+" = "+accountNo+ " ;",null);
        if(res.getCount()==0) {
            db.close();
            return null;
        }
        int idIndex= res.getColumnIndex(COLUMN_ACCOUNT_ID);
        int bankIndex=res.getColumnIndex(COLUMN_BANK_NAME);
        int accHolderIndex=res.getColumnIndex(COLUMN_AC_HOLDER_NAME);
        int balanceIndex=res.getColumnIndex(COLUMN_BALANCE);

        Account account=new Account(res.getString(idIndex),res.getString(bankIndex),res.getString(accHolderIndex),Double.valueOf(res.getString(balanceIndex)));
        db.close();
        return account;
    }

    public void addAccount(Account account) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ACCOUNT_ID,account.getAccountNo());
        contentValues.put(COLUMN_BANK_NAME,account.getBankName());
        contentValues.put(COLUMN_AC_HOLDER_NAME,account.getAccountHolderName());
        contentValues.put(COLUMN_BALANCE,account.getBalance());
        db.insert(TABLE_ACCOUNT, null,contentValues);
        db.close();

    }

    public boolean removeAccount(String accountNo){
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDel = db.delete(TABLE_ACCOUNT,COLUMN_ACCOUNT_ID+" = ? ",new String[]{accountNo});
        db.close();
        if (rowsDel==0)
            return false;
        return true;

    }

    public void updateBalance(Account account) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_BALANCE, account.getBalance());
        db.update(TABLE_ACCOUNT,values,COLUMN_ACCOUNT_ID+" = ?",new String[]{account.getAccountNo()});
        db.close();

    }

    public void addTransaction(Transaction transaction){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ACCOUNT_NO,transaction.getAccountNo());
        values.put(COLUMN_EXPENCE_TYPE,(transaction.getExpenseType()== ExpenseType.INCOME) ? 1 : -1);
        values.put(COLUMN_AMOUNT,transaction.getAmount());
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String date = sdf.format(transaction.getDate());
        values.put(COLUMN_DATE,date);
        db.insert(TABLE_TRANSACTION, null, values);
        db.close();
    }

    public List<Transaction> getAllTransactions(){
        List<Transaction> transactions = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_TRANSACTION + ";", null);
        res.moveToFirst();
        while (!res.isAfterLast()){
            String accoutNo = res.getString(res.getColumnIndex(COLUMN_ACCOUNT_NO));
            ExpenseType type = (res.getInt(res.getColumnIndex(COLUMN_EXPENCE_TYPE))==1)? ExpenseType.INCOME:ExpenseType.EXPENSE;
            double amount = res.getDouble(res.getColumnIndex(COLUMN_AMOUNT));
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date date = null;
            try {
                date = sdf.parse(res.getString(res.getColumnIndex(COLUMN_DATE)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            transactions.add(new Transaction(date,accoutNo,type,amount));
            res.moveToNext();
        }
        db.close();
        return transactions;
    }

    public List<Transaction> getPaginatedTransactions(int limit){
        SQLiteDatabase db = getReadableDatabase();
        Cursor res = db.rawQuery("SELECT COUNT(*) FROM "+TABLE_TRANSACTION+";",null);
        res.moveToFirst();
        int rowCount = res.getInt(0);
        if(rowCount<=limit){
            db.close();
            return getAllTransactions();
        }else{
            List<Transaction> transactions = new ArrayList<>();
            res = db.rawQuery("SELECT * FROM " + TABLE_TRANSACTION + " LIMIT 10 OFFSET "+Integer.toString(rowCount-10)+";", null);
            res.moveToFirst();
            while (!res.isAfterLast()){
                String accoutNo = res.getString(res.getColumnIndex(COLUMN_ACCOUNT_NO));
                ExpenseType type = (res.getInt(res.getColumnIndex(COLUMN_EXPENCE_TYPE))==1)? ExpenseType.INCOME:ExpenseType.EXPENSE;
                double amount = res.getDouble(res.getColumnIndex(COLUMN_AMOUNT));
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                Date date = null;
                try {
                    date = sdf.parse(res.getString(res.getColumnIndex(COLUMN_DATE)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                transactions.add(new Transaction(date,accoutNo,type,amount));
                res.moveToNext();
            }
            db.close();
            return  transactions;
        }
    }

}


