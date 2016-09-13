package spcstudent.android.personaltrainermanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;

import com.getbase.floatingactionbutton.FloatingActionButton;

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

        // ############# Floating Action Menu ############### //

        FloatingActionButton logout = (FloatingActionButton) findViewById(R.id.action_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.FragmentManager manager = getFragmentManager();
                FragmentLogout dialog = new FragmentLogout ();
                dialog.show(manager, "Logout");
            }
        });

        final FloatingActionButton sessionList = (FloatingActionButton)
                findViewById(R.id.action_session_list);
        sessionList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newSessionIntent = new Intent(MainActivity.this,
                        SessionList.class);
                MainActivity.this.startActivity(newSessionIntent);
            }
        });

        final FloatingActionButton newCustomer = (FloatingActionButton)
                findViewById(R.id.action_new_customer);
        newCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newCustomerIntent = new Intent(MainActivity.this,
                        NewCustomer.class);
                MainActivity.this.startActivity(newCustomerIntent);
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
