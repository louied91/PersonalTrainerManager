package spcstudent.android.personaltrainermanager;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import com.getbase.floatingactionbutton.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.UUID;

public class CustomerPage extends FragmentActivity {
    protected Customer mCustomer;
    private File mPhotoFile;
    private ImageView profilePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_page);

        // bind variables to views
        profilePicture = (ImageView) findViewById(R.id.customer_photo_imageview);
        TextView fname = (TextView) findViewById(R.id.first_name_textview);
        TextView lname = (TextView) findViewById(R.id.last_name_textview);


        // get extras from customer list
        final Bundle myBundle = getIntent().getExtras();

        // get customer object
        final CustomerList customerList = CustomerList.get(this);
        final UUID customerID = (UUID) myBundle.getSerializable(ApplicationKeys.CUSTOMER_ID);
        mCustomer = customerList.getCustomer(customerID);


        // TODO set correct profile picture for each user
        mPhotoFile = CustomerList.get(CustomerPage.this).getPhotoFile(mCustomer);
        updatePhotoView();

        // set customers name
        fname.setText(mCustomer.getFirstName());
        lname.setText(mCustomer.getLastName());


        /**
         * Fragments
         */

        FragmentManager fm = getSupportFragmentManager();
        Fragment header = fm.findFragmentById(R.id.header_fragment);
        Fragment sessionList = fm.findFragmentById(R.id.sessions_list_fragment);

        Bundle toFragBundle = new Bundle();
        toFragBundle.putSerializable(ApplicationKeys.CUSTOMER_ID, mCustomer.getCustomerId());

        // check if fragment exists within container
        // if not create an instance of the fragment and add it to the container
        if (header == null) {
            header = new FragmentHeader();
            fm.beginTransaction()
                    .add(R.id.header_container, header)
                    .commit();
        }

        if (sessionList == null) {
            sessionList = new FragmentSessionList();
            sessionList.setArguments(toFragBundle);
            fm.beginTransaction()
                    .add(R.id.session_list_container, sessionList)
                    .commit();
        }


        /**
         * Floating Action Menu
         */

        FloatingActionButton logout = (FloatingActionButton) findViewById(R.id.action_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.FragmentManager manager = getFragmentManager();
                FragmentLogout dialog = new FragmentLogout();
                dialog.show(manager, "Logout");
            }
        });

        FloatingActionButton editCustomer = (FloatingActionButton)
                findViewById(R.id.action_edit_customer);
        editCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editCustomerIntent = new Intent(CustomerPage.this,
                        NewCustomer.class);
                editCustomerIntent.putExtras(myBundle);
                CustomerPage.this.startActivity(editCustomerIntent);
            }
        });

        FloatingActionButton createNewSession = (FloatingActionButton)
                findViewById(R.id.action_new_session);
        createNewSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle toEditSession = new Bundle();
                toEditSession.putSerializable(ApplicationKeys.CUSTOMER_ID, mCustomer.getCustomerId());
                toEditSession.putSerializable(ApplicationKeys.SESSION_ID, null);
                Intent newSessionIntent = new Intent(CustomerPage.this,
                        ActivityEditSession.class);
                newSessionIntent.putExtras(toEditSession);
                CustomerPage.this.startActivity(newSessionIntent);
            }
        });

        FloatingActionButton removeCustomer = (FloatingActionButton)
                findViewById(R.id.action_remove_customer);
        removeCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customerList.deleteCustomer(getBaseContext() ,mCustomer);
                finish();
            }
        });

    }

    @Override
    public void onRestart() {
        super.onRestart();

        finish();
        startActivity(getIntent());
    }

    private void updatePhotoView() {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            profilePicture.setImageResource(R.mipmap.profile_img);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(),
                    CustomerPage.this);
            profilePicture.setImageBitmap(bitmap);
        }
    }

}