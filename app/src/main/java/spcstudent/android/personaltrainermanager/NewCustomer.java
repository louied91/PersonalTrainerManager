package spcstudent.android.personaltrainermanager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NewCustomer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_list);

        FragmentManager fm = getSupportFragmentManager();
        Fragment header = fm.findFragmentById(R.id.username_textview);

        if (header == null) {
            header = new DisplayUsername();
            fm.beginTransaction()
                    .add(R.id.header_container, header)
                    .commit();
        }
    }

}
