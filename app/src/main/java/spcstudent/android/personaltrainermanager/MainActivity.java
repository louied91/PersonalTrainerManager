package spcstudent.android.personaltrainermanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;

public class MainActivity extends FragmentActivity {

    private Button mCustomerList;
    private Button mSessionList;
    private Button mNewCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);

        FragmentManager fm = getSupportFragmentManager();
        Fragment header = fm.findFragmentById(R.id.username_textview);

        if (header == null) {
            header = new DisplayUsername();
            fm.beginTransaction()
                    .add(R.id.header_container, header)
                    .commit();
        }

        mCustomerList = (Button)findViewById(R.id.listCustomersButton);
        mCustomerList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent CustomerListIntent = new Intent(MainActivity.this, CustomerList.class);
                MainActivity.this.startActivity(CustomerListIntent);
            }
        });

        mSessionList = (Button)findViewById(R.id.viewCustomerSessionsButton);
        mSessionList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent SessionListIntent = new Intent(MainActivity.this, SessionList.class);
                MainActivity.this.startActivity(SessionListIntent);
            }
        });

        mNewCustomer = (Button)findViewById(R.id.newCustomerButton);
        mNewCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent NewCustomerIntent = new Intent(MainActivity.this, NewCustomer.class);
                MainActivity.this.startActivity(NewCustomerIntent);
            }
        });

    }


}