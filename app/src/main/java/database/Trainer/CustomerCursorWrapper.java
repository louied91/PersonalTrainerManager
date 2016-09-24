package database.Trainer;


import android.database.Cursor;
import android.database.CursorWrapper;

import spcstudent.android.personaltrainermanager.Customer;


import java.util.Date;
import java.util.UUID;

public class CustomerCursorWrapper extends CursorWrapper {
    public CustomerCursorWrapper (Cursor cursor) {
        super(cursor);
    }

    public Customer getCustomer() {
        String customerIdString = getString(getColumnIndex(
                TrainerDatabase.CustomerTable.Columns.CUSTOMER_ID));
        String firstNameString = getString(getColumnIndex(
                TrainerDatabase.CustomerTable.Columns.FIRST_NAME));
        String lastNameString = getString(getColumnIndex(
                TrainerDatabase.CustomerTable.Columns.LAST_NAME));
        long birthdateLong = getLong(getColumnIndex(
                TrainerDatabase.CustomerTable.Columns.BIRTHDATE));
        String billingString = getString(getColumnIndex(
                TrainerDatabase.CustomerTable.Columns.BILLING_INFO));
        String emailString = getString(getColumnIndex(
                TrainerDatabase.CustomerTable.Columns.EMAIL));

        Customer customer = new Customer(UUID.fromString(customerIdString));

        customer.setFirstName(firstNameString);
        customer.setLastName(lastNameString);
        customer.setBirthDate(new Date(birthdateLong));
        customer.setBillingInformation(billingString);
        customer.setEmail(emailString);

        return customer;
    }
}