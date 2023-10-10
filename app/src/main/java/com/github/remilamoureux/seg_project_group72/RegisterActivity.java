package com.github.remilamoureux.seg_project_group72;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void onRegister(View view) {

        if (!validateFields()) {
            Toast toast = Toast.makeText(getApplicationContext(), "Invalid username, email or password.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        String email = ((EditText)findViewById(R.id.emailField)).getText().toString();
        String usr = ((EditText)findViewById(R.id.usernameField)).getText().toString();
        String pwd = ((EditText)findViewById(R.id.passwordField)).getText().toString();
        boolean role = ((Switch)findViewById(R.id.switch1)).isChecked();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference roleRef = database.getReference("users/" + usr + "/role");
        DatabaseReference emailRef = database.getReference("users/" + usr + "/email");
        DatabaseReference passwRef = database.getReference("users/" + usr + "/password");

        if (role) {
            roleRef.setValue("Group");
        } else {
            roleRef.setValue("Participant");
        }
        emailRef.setValue(email);
        passwRef.setValue(pwd);

        Intent intent = getIntent();
        intent.putExtra("username", usr);
        intent.putExtra("password", pwd);
        setResult(RESULT_OK, intent);
        finish();
    }

    private boolean validateFields() {
        EditText f1 = findViewById(R.id.usernameField);
        EditText f2 = findViewById(R.id.passwordField);
        EditText f3 = findViewById(R.id.emailField);

        if (f1.length() == 0) {
            f1.setError("Must have username");
            return false;
        }
        if (f2.length() == 0) {
            f2.setError("Must have password");
            return false;
        }
        if (!f1.getText().toString().matches("[A-Za-z0-9]+")) {
            f1.setError("Username must be alphanumeric");
            return false;
        }
        if (!f2.getText().toString().matches("[A-Za-z0-9]+")) {
            f2.setError("Password must be alphanumeric");
            return false;
        }

        if (f3.getText().toString().matches("\t^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$")) {
            f3.setError("Not a valid email address.");
            return false;
        }

        f1.setError(null);
        f2.setError(null);
        f3.setError(null);
        return true;
    }
}