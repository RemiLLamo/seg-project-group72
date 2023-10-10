package com.github.remilamoureux.seg_project_group72;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.github.remilamoureux.seg_project_group72.data.LoggedInUser;
import com.github.remilamoureux.seg_project_group72.data.LoginResultGrabber;

public class LoggedInActivity extends AppCompatActivity implements LoginResultGrabber {

    LoggedInUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);
        LoggedInUser.attemptLogin(getIntent().getStringExtra("username"), getIntent().getStringExtra("password"), this);
        TextView v = findViewById(R.id.LoggedInWelcome);
        String welcomeMessage = "Loading user information...";
        v.setText(welcomeMessage);
    }

    public void result(LoggedInUser user) {
        if (user == null) {
            finish();
        }
        String welcomeMessage = "Welcome " + user.getUsername() + "! You are logged in as \"" + user.getRole() + "\".";
        TextView v = findViewById(R.id.LoggedInWelcome);
        v.setText(welcomeMessage);
    }
}