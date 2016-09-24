package spcstudent.android.personaltrainermanager;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.File;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class ActivityEditSession extends FragmentActivity {
    private UUID customerId;
    private UUID sessionId;
    private Customer mCustomer;
    private Session mSession;
    private Boolean isNewSession = false;
    private File mPhotoFile;

    private ImageView profilePicture;
    private Button saveSessionButton;
    private EditText sessionName, sessionDate, sessionTime, sessionCost;
    private Calendar mCalendar;
    private DatePickerDialog.OnDateSetListener datePickListener;
    private TimePickerDialog.OnTimeSetListener timePickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_session);

        // view variables
        profilePicture = (ImageView) findViewById(R.id.customer_photo_imageview);
        TextView fname = (TextView) findViewById(R.id.first_name_textview);
        TextView lname = (TextView) findViewById(R.id.last_name_textview);
        sessionName = (EditText) findViewById(R.id.edit_session_name_field);
        sessionDate = (EditText) findViewById(R.id.edit_session_date_field);
        sessionTime = (EditText) findViewById(R.id.edit_session_time_field);
        sessionCost = (EditText) findViewById(R.id.edit_session_cost_field);
        saveSessionButton = (Button) findViewById(R.id.save_session_button);

        // get ids from customer page
        Bundle myBundle = getIntent().getExtras();
        customerId = (UUID) myBundle.getSerializable(ApplicationKeys.CUSTOMER_ID);
        sessionId = (UUID) myBundle.getSerializable(ApplicationKeys.SESSION_ID);

        // get customer and session
        CustomerList customerList = CustomerList.get(this);
        final SessionList sessionList = SessionList.get(this);
        mCustomer = customerList.getCustomer(customerId);
        mSession = sessionList.getSession(sessionId);
        if (mSession == null) {
            mSession = new Session(customerId, false);
            isNewSession = true;
        }

        // TODO link profile picture to actual customer
        mPhotoFile = CustomerList.get(ActivityEditSession.this).getPhotoFile(mCustomer);
        updatePhotoView();

        // set first / last name
        fname.setText(mCustomer.getFirstName());
        lname.setText(mCustomer.getLastName());

        // populate fields with existing session data if it exists
        try {
            sessionName.setText(mSession.getSessionName(), TextView.BufferType.EDITABLE);
            sessionDate.setText(
                    formatSessionDate(mSession.getSessionDateTime()),
                    TextView.BufferType.EDITABLE);
            sessionTime.setText(
                    formatSessionTime(mSession.getSessionDateTime()),
                    TextView.BufferType.EDITABLE);
            sessionCost.setText(Double.toString(mSession.getSessionCost()),
                    TextView.BufferType.EDITABLE);
        } catch (NullPointerException ref) {}

        // listen for date picker to set date
        // update customer and date field with new date
        datePickListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Date newSessionDate = new Date(year - 1900, monthOfYear, dayOfMonth);
                mSession.setSessionDateTime(newSessionDate);
                sessionDate.setText(
                        formatSessionDate(newSessionDate), TextView.BufferType.EDITABLE);
            }
        };

        // listen for date picker to set date
        // update customer and date field with new date
        timePickListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Date newSessionTime = mSession.getSessionDateTime();
                newSessionTime.setHours(hourOfDay);
                newSessionTime.setMinutes(minute);
                mSession.setSessionDateTime(newSessionTime);
                sessionTime.setText(
                        formatSessionTime(newSessionTime), TextView.BufferType.EDITABLE);
            }
        };

        // set date and time fields to act like buttons
        // bring up date and time picker for date
        sessionDate.setFocusable(false);
        sessionDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendar = Calendar.getInstance();
                try {
                    new DatePickerDialog(ActivityEditSession.this,
                            datePickListener,
                            mSession.getSessionDateTime().getYear() + 1900,
                            mSession.getSessionDateTime().getMonth(),
                            mSession.getSessionDateTime().getDay())
                            .show();
                } catch (NullPointerException ref) {
                    new DatePickerDialog(ActivityEditSession.this,
                            datePickListener,
                            mCalendar.get(Calendar.YEAR),
                            mCalendar.get(Calendar.MONTH),
                            mCalendar.get(Calendar.DAY_OF_MONTH))
                            .show();
                }
            }
        });

        // ... continued with time field
        sessionTime.setFocusable(false);
        sessionTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    new TimePickerDialog(ActivityEditSession.this,
                            timePickListener,
                            mSession.getSessionDateTime().getHours(),
                            mSession.getSessionDateTime().getMinutes(),
                            false)
                            .show();
                } catch (NullPointerException ref) {
                    new TimePickerDialog(ActivityEditSession.this,
                            timePickListener,
                            0, 0, false).show();
                }
            }
        });

        // button for saving modified session data
        saveSessionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mSession.setSessionName(sessionName.getText().toString());
                    mSession.setSessionCost(Double.parseDouble(sessionCost.getText().toString()));
                    if (isNewSession) {
                        SessionList.get(getParent()).addSession(mSession);
                    } else {
                        SessionList.get(getParent()).updateSession(mSession);
                    }
                    Toast.makeText(ActivityEditSession.this, "Changes Saved!",
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

        FloatingActionButton logout = (FloatingActionButton) findViewById(R.id.action_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.FragmentManager manager = getFragmentManager();
                FragmentLogout dialog = new FragmentLogout();
                dialog.show(manager, "Logout");
            }
        });

        final FloatingActionButton finishSession = (FloatingActionButton)
                findViewById(R.id.action_finish_session);
        finishSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent finishSessionIntent = new Intent(ActivityEditSession.this,
                        ActivitySignature.class);
                finishSessionIntent.putExtra(ApplicationKeys.SESSION_ID, sessionId);
                ActivityEditSession.this.startActivity(finishSessionIntent);
            }
        });

        FloatingActionButton deleteSession = (FloatingActionButton)
                findViewById(R.id.action_delete_session);
        deleteSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionList.deleteSession(mSession);
                finish();
            }
        });
    }

    // format session date
    private String formatSessionDate(Date sessionDate) {
        DateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
        return dateFormat.format(sessionDate);
    }

    private String formatSessionTime(Date sessionDate) {
        DateFormat dateFormat = new SimpleDateFormat("h:mm a");
        return dateFormat.format(sessionDate);
    }

    private void updatePhotoView() {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            profilePicture.setImageResource(R.mipmap.profile_img);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(),
                    ActivityEditSession.this);
            profilePicture.setImageBitmap(bitmap);
        }
    }

}