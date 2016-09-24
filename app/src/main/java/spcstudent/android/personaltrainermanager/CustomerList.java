package spcstudent.android.personaltrainermanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import database.Trainer.CustomerCursorWrapper;
import database.Trainer.TrainerDBHelper;
import database.Trainer.TrainerDatabase;
import database.Trainer.TrainerDatabase.CustomerTable;

public class CustomerList {
    // holds instance of CustomerList
    private static CustomerList sCustomerList;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    // if an instance of CustomerList doesn't exists, create a new instance
    // return the new instance or an already existing instance
    public static CustomerList get(Context context) {
        if (sCustomerList == null) {
            sCustomerList = new CustomerList(context);
        }
        return sCustomerList;
    }

    // must use get method to construct
    private CustomerList(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new TrainerDBHelper(mContext).getWritableDatabase();
    }

    public void addCustomer(Customer customer) {
        ContentValues values = getContentValues(customer);
        mDatabase.insert(TrainerDatabase.CustomerTable.NAME, null, values);
    }

    public List<Customer> getCustomers() {
        List<Customer> customers = new ArrayList<>();

        CustomerCursorWrapper cursor = queryCustomers(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                customers.add(cursor.getCustomer());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return customers;
    }

    public Customer getCustomer(UUID id) {
        CustomerCursorWrapper cursor = queryCustomers(
                CustomerTable.Columns.CUSTOMER_ID + " = ?",
                new String[] { id.toString() }
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getCustomer();
        } finally {
            cursor.close();
        }
    }

    public void deleteCustomer(Context context, Customer customer) {
        SessionList sessionList = SessionList.get(context);
        String uuidString = customer.getCustomerId().toString();
        sessionList.deleteCustomersSessions(customer);
        mDatabase.delete(CustomerTable.NAME,
                CustomerTable.Columns.CUSTOMER_ID + " = ?",
                new String[] { uuidString });
    }

    public void updateCustomer(Customer customer) {
        String uuidString = customer.getCustomerId().toString();
        ContentValues values = getContentValues(customer);

        mDatabase.update(TrainerDatabase.CustomerTable.NAME, values,
                CustomerTable.Columns.CUSTOMER_ID + " = ?",
                new String[] { uuidString });
    }

    private static ContentValues getContentValues(Customer customer) {
        ContentValues values = new ContentValues();
        values.put(CustomerTable.Columns.CUSTOMER_ID, customer.getCustomerId().toString());
        values.put(CustomerTable.Columns.FIRST_NAME, customer.getFirstName());
        values.put(CustomerTable.Columns.LAST_NAME, customer.getLastName());
        values.put(CustomerTable.Columns.BIRTHDATE, customer.getBirthDate().getTime());
        values.put(CustomerTable.Columns.BILLING_INFO, customer.getBillingInformation());
        values.put(CustomerTable.Columns.EMAIL, customer.getEmail());

        return values;
    }

    private CustomerCursorWrapper queryCustomers (String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                CustomerTable.NAME,
                null, // columns select - all
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new CustomerCursorWrapper(cursor);
    }

    public File getPhotoFile (Customer customer) {
        File externalFilesDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if (externalFilesDir == null) {
            return null;
        }

        return new File(externalFilesDir, customer.getPhotoFilename());
    }
}