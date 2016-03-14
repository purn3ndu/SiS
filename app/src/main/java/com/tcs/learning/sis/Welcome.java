package com.tcs.learning.sis;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class Welcome extends Activity implements View.OnClickListener {

    private TextView mTextDetails;
    private CallbackManager mCallbackManager;
    private AccessTokenTracker mTokenTracker;
    private ProfileTracker mProfileTracker;

    public Spinner spinner;
    private EditText editTextKeyword;
    private EditText editTextCourseTime;
    private Button buttonSearch;


    public final static String ex_time = "com.example.shortissweet.MESSAGE1";
    public final static String ex_keywords = "com.example.shortissweet.MESSAGE2";
    public final static String ex_level = "com.example.shortissweet.MESSAGE3";

    private long mBackPressed;
    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.


    private FacebookCallback<LoginResult> mFacebookCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            Log.d("PURE", "onSuccess");
            AccessToken accessToken = loginResult.getAccessToken();

            Profile profile = Profile.getCurrentProfile();
            if (accessToken == null) {
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        }


        @Override
        public void onCancel() {
            Log.d("PURE", "onCancel");
        }

        @Override
        public void onError(FacebookException e) {
            Log.d("PURE", "onError " + e);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        FacebookSdk.sdkInitialize(getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();
        setupTokenTracker();
        setupProfileTracker();
        setupLoginButton();

        mTokenTracker.startTracking();
        mProfileTracker.startTracking();

        mTextDetails = (TextView) findViewById(R.id.hi_user);


        spinner = (Spinner) findViewById(R.id.spinner);
        editTextKeyword = (EditText) findViewById(R.id.editTextKeyword);
        editTextCourseTime = (EditText) findViewById(R.id.editTextCourseTime);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.difficulty_level, R.layout.element_spinner);
        adapter.setDropDownViewResource(R.layout.element_spinner_dropdown);
        spinner.setAdapter(adapter);
        buttonSearch = (Button) findViewById(R.id.buttonSearch);
        buttonSearch.setOnClickListener(this);
    }
    @Override
    public void onBackPressed()
    {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis())
        {
            super.onBackPressed();
            //return;
            android.os.Process.killProcess(android.os.Process.myPid());
            return;
        }
        else { Toast.makeText(getBaseContext(), "Tap back button again in order to exit", Toast.LENGTH_SHORT).show(); }

        mBackPressed = System.currentTimeMillis();
    }

    @Override
    public void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        mTextDetails.setText(constructWelcomeMessage(profile));
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    public void sendMessage() {

        String keywords = "";
        if (editTextKeyword.getText() != null) {
            keywords = editTextKeyword.getText().toString();
        }
        if (keywords.contentEquals("")) {
            Toast.makeText(getApplicationContext(), "Please Enter keyword!", Toast.LENGTH_SHORT).show();
            return;
        }

        String courseTime = "";
        if (editTextCourseTime.getText() != null) {
            courseTime = editTextCourseTime.getText().toString();
        }
        if (courseTime.contentEquals("")) {
            Toast.makeText(getApplicationContext(), "Please Enter a valid amount of time!", Toast.LENGTH_SHORT).show();
            return;
        }

        String level = "";
        if (spinner.getSelectedItem() != null) {
            level = spinner.getSelectedItem().toString();
        }
        if (level.contentEquals("")) {
            Toast.makeText(getApplicationContext(), "Please Select Level!", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, ShowStudyMaterials.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(ex_time, courseTime);
        intent.putExtra(ex_keywords, keywords);
        intent.putExtra(ex_level, level);
        Log.d("temp", "Sending Intent: " + courseTime + " " + keywords + " " + level);
        startActivity(intent);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.buttonSearch:
                sendMessage();
                break;
        }
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }



    private void setupTokenTracker() {
        mTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                Log.d("PURE", "" + currentAccessToken);
            }
        };
    }

    private void setupProfileTracker() {
        mProfileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                Log.d("PURE", "" + currentProfile);
            }
        };
    }
    private void setupLoginButton() {
        LoginButton mButtonLogin = (LoginButton) findViewById(R.id.login_button);
        mButtonLogin.setReadPermissions("user_friends");
        mButtonLogin.registerCallback(mCallbackManager, mFacebookCallback);
    }

    private String constructWelcomeMessage(Profile profile) {
        StringBuilder stringBuffer = new StringBuilder();
        if (profile != null) {
            stringBuffer.append("Welcome ").append(profile.getFirstName());
        }else {
            stringBuffer.append("Welcome");
        }
        return stringBuffer.toString();
    }
}
