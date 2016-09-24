package spcstudent.android.personaltrainermanager;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import com.getbase.floatingactionbutton.FloatingActionButton;

import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

public class NewCustomer extends FragmentActivity {
    private Customer mCustomer;
    private Boolean isNewCustomer = false;
    private File mPhotoFile;

    private ImageView profilePicture;
    private Button galleryButton, cameraButton, saveCustomerButton;
    private EditText firstNameField, lastNameField, dateOfBirthField, billingField, emailField;
    private Calendar calendar;
    private DatePickerDialog.OnDateSetListener datePickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_customer);

        // bind ui to vars
        profilePicture = (ImageView) findViewById(R.id.customer_photo_imageview);
        galleryButton = (Button) findViewById(R.id.from_gallery_button);
        cameraButton = (Button) findViewById(R.id.from_camera_button);
        firstNameField = (EditText) findViewById(R.id.edit_first_name_field);
        lastNameField = (EditText) findViewById(R.id.edit_last_name_field);
        dateOfBirthField = (EditText) findViewById(R.id.edit_date_of_birth_field);
        billingField = (EditText) findViewById(R.id.edit_billing_info_field);
        emailField = (EditText) findViewById(R.id.edit_email_field);
        saveCustomerButton = (Button) findViewById(R.id.save_customer_button);

        // get customer
        Bundle bundle = getIntent().getExtras();
        UUID customerId = (UUID) bundle.getSerializable(ApplicationKeys.CUSTOMER_ID);
        mCustomer = CustomerList.get(this).getCustomer(customerId);
        if (mCustomer == null) {
            mCustomer = new Customer();
            isNewCustomer = true;
        }

        // set profile picture
        mPhotoFile = CustomerList.get(NewCustomer.this).getPhotoFile(mCustomer);
        updatePhotoView();

        // camera button
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        boolean canTakePhoto = mPhotoFile != null &&
                captureImage.resolveActivity(getPackageManager()) != null;
        cameraButton.setEnabled(canTakePhoto);

        if (canTakePhoto) {
            Uri uriPhoto = Uri.fromFile(mPhotoFile);
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uriPhoto);
        }

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(captureImage, ApplicationKeys.REQUEST_PHOTO);
            }
        });

        // gallery button
        final Intent getImage = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        boolean hasExternalStorage = mPhotoFile != null;
        galleryButton.setEnabled(hasExternalStorage);

        if (hasExternalStorage) {
            Uri uriGallery = Uri.fromFile(mPhotoFile);
            getImage.putExtra(MediaStore.EXTRA_OUTPUT, uriGallery);
        }

        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(getImage, ApplicationKeys.REQUEST_GALLERY);
            }
        });

        // populate fields with existing customer data
        try {
            firstNameField.setText(mCustomer.getFirstName(),
                    TextView.BufferType.EDITABLE);
            lastNameField.setText(mCustomer.getLastName(),
                    TextView.BufferType.EDITABLE);
            dateOfBirthField.setText(
                    formatBirthdate(mCustomer.getBirthDate()),
                    TextView.BufferType.EDITABLE);
            billingField.setText(mCustomer.getBillingInformation(),
                    TextView.BufferType.EDITABLE);
            emailField.setText(mCustomer.getEmail(),
                    TextView.BufferType.EDITABLE);
        } catch (NullPointerException ref) {}

        // listen for date picker to set date
        // update customer and date field with new date
        datePickListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Date newBirthDate = new Date(year - 1900, monthOfYear, dayOfMonth);
                mCustomer.setBirthDate(newBirthDate);
                dateOfBirthField.setText(
                        formatBirthdate(newBirthDate), TextView.BufferType.EDITABLE);
            }
        };

        // make date field open a date picker on click
        // try to set start at current customers birth date
        // else set it to current date
        dateOfBirthField.setFocusable(false);
        dateOfBirthField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                try {
                    new DatePickerDialog(NewCustomer.this,
                            datePickListener,
                            mCustomer.getBirthDate().getYear() + 1900,
                            mCustomer.getBirthDate().getMonth(),
                            mCustomer.getBirthDate().getDay())
                            .show();
                } catch (NullPointerException ref) {
                    new DatePickerDialog(NewCustomer.this,
                            datePickListener,
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH))
                            .show();
                }
            }
        });

        saveCustomerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mCustomer.setFirstName(firstNameField.getText().toString());
                    mCustomer.setLastName(lastNameField.getText().toString());
                    mCustomer.setBillingInformation(billingField.getText().toString());
                    mCustomer.setEmail(emailField.getText().toString());
                    if (isNewCustomer) {
                        CustomerList.get(getParent()).addCustomer(mCustomer);
                    } else {
                        CustomerList.get(getParent()).updateCustomer(mCustomer);
                    }
                    Toast.makeText(NewCustomer.this, "Changes Saved!",
                            Toast.LENGTH_SHORT).show();
                    finish();
                } catch (NullPointerException ref) {
                    ref.printStackTrace();
                    android.app.FragmentManager manager = getFragmentManager();
                    FragmentChangesExceptPrompt dialog = new FragmentChangesExceptPrompt();
                    dialog.show(manager, "Changes Exception");
                }
            }
        });

        FloatingActionButton logout = (FloatingActionButton) findViewById(R.id.action_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.FragmentManager manager = getFragmentManager();
                FragmentLogout dialog = new FragmentLogout();
                dialog.show(manager, "Logout");
            }
        });

        // header fragment
        FragmentManager fm = getSupportFragmentManager();
        Fragment header = fm.findFragmentById(R.id.header_container);

        // check if fragment exists within container
        // if not create an instance of the fragment and add it to the container
        if (header == null) {
            header = new FragmentHeader();
            fm.beginTransaction()
                    .add(R.id.header_container, header)
                    .commit();
        }
    }

    @Override
    public void onRestart() {
        super.onRestart();

        finish();
        startActivity(getIntent());
    }

    // format birth date
    private String formatBirthdate(Date birthdate) {
        DateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
        return dateFormat.format(birthdate);
    }

    private void updatePhotoView() {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            profilePicture.setImageResource(R.mipmap.profile_img);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(),
                    NewCustomer.this);
            profilePicture.setImageBitmap(bitmap);
        }
    }

}