package spcstudent.android.personaltrainermanager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.UUID;

public class ActivitySignature extends FragmentActivity {
    private SessionList mSessionList;
    private Session mSession;
    private UUID mSessionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);

        mSessionList = SessionList.get(this);
        Bundle myBundle = getIntent().getExtras();
        mSessionId = (UUID) myBundle.getSerializable(ApplicationKeys.SESSION_ID);
        mSession = mSessionList.getSession(mSessionId);

        // header fragment
        FragmentManager fm = getSupportFragmentManager();
        Fragment header = fm.findFragmentById(R.id.header_fragment);

        // check if fragment exists within container
        // if not create an instance of the fragment and add it to the container
        if (header == null) {
            header = new FragmentHeader();
            fm.beginTransaction()
                    .add(R.id.header_container, header)
                    .commit();
        }

        Button cancel = (Button) findViewById(R.id.cancel_button);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySignature.this.finish();
            }
        });

        Button authorize = (Button) findViewById(R.id.authorize_button);
        authorize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSession.setIsSessionCompleted(true);
                mSessionList.updateSession(mSession);
                Toast.makeText(ActivitySignature.this, "Session Completed!",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
}