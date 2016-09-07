package spcstudent.android.personaltrainermanager;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


public class LoginActivity extends AppCompatActivity {

    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "jdoe:welcome1",
    };

    private UserLoginTask mAuthTask = null;


    private EditText mUserView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private ImageView mLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mLogo = (ImageView) findViewById(R.id.AppIcon);

        // Set up the login form.
        mUserView = (EditText) findViewById(R.id.user);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        // TODO: remove autofill testing credentials for jdoe
        mUserView.setText("jdoe", TextView.BufferType.EDITABLE);
        mPasswordView.setText("welcome1", TextView.BufferType.EDITABLE);

        Button mUserSignInButton = (Button) findViewById(R.id.user_sign_in_button);
        mUserSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }


    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }


        mUserView.setError(null);
        mPasswordView.setError(null);


        String user = mUserView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;


        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }


        if (TextUtils.isEmpty(user)) {
            mUserView.setError(getString(R.string.error_field_required));
            focusView = mUserView;
            cancel = true;
        } else if (!isUserValid(user)) {
            mUserView.setError(getString(R.string.error_invalid_user));
            focusView = mUserView;
            cancel = true;
        }

        if (cancel) {

            focusView.requestFocus();
        } else {

            showProgress(true);
            mAuthTask = new UserLoginTask(user, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isUserValid(String userName) {
        boolean isValid = true;
        if (userName.length() < 4) {
            isValid = false;
        }
        return isValid;
    }

    private boolean isPasswordValid(String password) {
        boolean isValid = true;
        if (password.length() < 7) {
            isValid = false;
        }
        return isValid;
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            //mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
        mLogo.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUser;
        private final String mPassword;

        UserLoginTask(String user, String password) {
            mUser = user;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mUser)) {
                    return pieces[1].equals(mPassword);
                }
            }

            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
                Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
                LoginActivity.this.startActivity(myIntent);
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

}

