package database.Trainer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import database.Trainer.TrainerDatabase.CustomerTable;
import database.Trainer.TrainerDatabase.SessionTable;

public class TrainerDBHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "trainerdatabase.db";

    public TrainerDBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //db.execSQL("PRAGMA foreign_keys = ON;");

        db.execSQL("create table " + CustomerTable.NAME + "( " +
                CustomerTable.Columns.CUSTOMER_ID + " text primary key, " +
                CustomerTable.Columns.FIRST_NAME + ", " +
                CustomerTable.Columns.LAST_NAME + ", " +
                CustomerTable.Columns.BIRTHDATE + ", " +
                CustomerTable.Columns.BILLING_INFO + ", " +
                CustomerTable.Columns.EMAIL +
                ");");

        // Todo fix foreign key in session table
        db.execSQL("create table " + SessionTable.NAME + "( " +
                SessionTable.Columns.SESSION_ID + " text primary key, " +
                SessionTable.Columns.CUSTOMER_ID + ", " +
                SessionTable.Columns.SESSION_NAME + ", " +
                SessionTable.Columns.SESSION_DATE + ", " +
                SessionTable.Columns.SESSION_COST + ", " +
                SessionTable.Columns.SESSION_COMPLETED +
                ");");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("TaskDBAdapter", "Upgrading from version " + oldVersion + " to " +
                newVersion + ", which will destroy all old data");

        db.execSQL("drop table if it exists " + CustomerTable.NAME + ", " +
                "drop table if it exists" + SessionTable.NAME);

        onCreate(db);
    }
}