package spcstudent.android.personaltrainermanager;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

public class CustomerPage extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_page);

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