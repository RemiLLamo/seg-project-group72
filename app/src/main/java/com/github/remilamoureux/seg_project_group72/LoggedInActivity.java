package com.github.remilamoureux.seg_project_group72;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.github.remilamoureux.seg_project_group72.data.Account;
import com.github.remilamoureux.seg_project_group72.data.DatabaseHandler;
import com.github.remilamoureux.seg_project_group72.data.accounttypes.Admin;
import com.github.remilamoureux.seg_project_group72.data.accounttypes.Club;

public class LoggedInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);
        TextView v = findViewById(R.id.LoggedInWelcome);
        Account user = DatabaseHandler.getHandler().getAccount();
        if (user == null) {
            finish();
        }
        String welcomeMessage = "Welcome " + user.getUsername() + "! You are logged in as \"" + user.getRoleName() + "\".";
        v.setText(welcomeMessage);
        if (user instanceof Admin) {
            Intent intent = new Intent(getApplicationContext(), LoggedInAdmin.class);
            startActivityForResult(intent, 0);
        } else if (user instanceof Club) {
            Intent intent = new Intent(getApplicationContext(), LoggedInClub.class);
            startActivityForResult(intent, 0);
        }
    }

}