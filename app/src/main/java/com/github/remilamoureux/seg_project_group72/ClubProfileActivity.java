package com.github.remilamoureux.seg_project_group72;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.github.remilamoureux.seg_project_group72.data.DatabaseHandler;

public class ClubProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_profile);
    }

    public void onRegister(View view) {

        if (!validateFields()) {
            Toast toast = Toast.makeText(getApplicationContext(), "Invalid name or phone number.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        String name = ((EditText)findViewById(R.id.usernameField)).getText().toString();
        String phone = ((EditText)findViewById(R.id.phoneField)).getText().toString();

        DatabaseHandler.getHandler().updateClub(name, phone);

        Intent intent = getIntent();
        setResult(RESULT_OK, intent);
        finish();
    }

    private boolean validateFields() {
        EditText f1 = findViewById(R.id.usernameField);
        EditText f2 = findViewById(R.id.phoneField);

        if (f1.length() == 0) {
            f1.setError("Must have username");
            return false;
        }
        if (f2.length() == 0) {
            f2.setError("Must have phone number");
            return false;
        }
        if (!f1.getText().toString().matches("[A-Za-z0-9 ]+")) {
            f1.setError("Username must be alphanumeric");
            return false;
        }

        if (!f2.getText().toString().matches("[0-9]+") || f2.getText().toString().length() != 10) {
            f2.setError("Must use a valid phone number");
            return false;
        }

        f1.setError(null);
        f2.setError(null);
        return true;
    }
}