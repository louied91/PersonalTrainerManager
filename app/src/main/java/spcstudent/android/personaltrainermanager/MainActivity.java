package spcstudent.android.personaltrainermanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;

public class MainActivity extends FragmentActivity {

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


        Button addCustomerButton = (Button) findViewById(R.id.newCustomerButton);

        addCustomerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewCustomer.class);
                startActivity(intent);
            }
        });
    }

}