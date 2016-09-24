package spcstudent.android.personaltrainermanager;

import android.content.Intent;
import android.os.Bundle;
import com.getbase.floatingactionbutton.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ActivityCustomerList extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_list);

        // ################# Fragments ##################### //

        FragmentManager fm = getSupportFragmentManager();
        Fragment header = fm.findFragmentById(R.id.header_fragment);
        Fragment customerList = fm.findFragmentById(R.id.customer_list_fragment);

        // check if header exists within container
        // if not create an instance of the fragment and add it to the container
        if (header == null) {
            header = new FragmentHeader();
            fm.beginTransaction()
                    .add(R.id.header_container, header)
                    .commit();
        }

        if (customerList == null) {
            customerList = new FragmentCustomerList();
            fm.beginTransaction()
                    .add(R.id.customer_list_container, customerList)
                    .commit();
        }

        // ############# Floating Action Menu ############### //

        FloatingActionButton logout = (FloatingActionButton) findViewById(R.id.action_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.FragmentManager manager = getFragmentManager();
                FragmentLogout dialog = new FragmentLogout();
                dialog.show(manager, "Logout");
            }
        });

        final FloatingActionButton newCustomer = (FloatingActionButton)
                findViewById(R.id.action_new_customer);
        newCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Customer customer = new Customer();
                Bundle myBundle = new Bundle();
                myBundle.putSerializable(ApplicationKeys.CUSTOMER_ID, customer.getCustomerId());
                Intent newCustomerIntent = new Intent(ActivityCustomerList.this,
                        NewCustomer.class);
                newCustomerIntent.putExtras(myBundle);
                ActivityCustomerList.this.startActivity(newCustomerIntent);
            }
        });
    }

    @Override
    public void onRestart() {
        super.onRestart();

        finish();
        startActivity(getIntent());
    }
}