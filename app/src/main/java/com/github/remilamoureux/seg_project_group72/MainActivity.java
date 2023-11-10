package com.github.remilamoureux.seg_project_group72;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.github.remilamoureux.seg_project_group72.data.Account;
import com.github.remilamoureux.seg_project_group72.data.DatabaseHandler;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //FirebaseDatabase database = FirebaseDatabase.getInstance();
        //DatabaseReference newUserRoleRef = database.getReference("users/" + username)
    }

    @Override
    protected void onStart() {
        super.onStart();
        DatabaseHandler.getHandler();
    }

    public void onSignin(View view) {

        if (!validateFields()) {
            Toast toast = Toast.makeText(getApplicationContext(), "Invalid input fields.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        String usr = ((EditText)findViewById(R.id.usernameField)).getText().toString();
        String pwd = ((EditText)findViewById(R.id.passwordField)).getText().toString();

        logIn(usr, pwd);
    }

    public void onCreateAccount(View view) {
        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) return;

        String usr = data.getStringExtra("username");
        String pwd = data.getStringExtra("password");

        logIn(usr, pwd);
    }

    private void logIn(String username, String password) {
        Account acc = DatabaseHandler.getHandler().attemptLogin(username, password);
        if (acc == null) {
            Toast toast = Toast.makeText(getApplicationContext(), "Invalid username or password.", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            Intent intent = new Intent(getApplicationContext(), LoggedInActivity.class);
            startActivityForResult(intent, 0);
        }
    }

    private boolean validateFields() {
        EditText f1 = findViewById(R.id.usernameField);
        EditText f2 = findViewById(R.id.passwordField);

        if (f1.length() == 0) {
            f1.setError("Must have username");
            return false;
        }
        if (f2.length() == 0) {
            f2.setError("Must have password");
            return false;
        }
        if (!f1.getText().toString().matches(("[A-Za-z0-9]+"))) {
            f1.setError("Username must be alphanumeric");
            return false;
        }
        if (!f2.getText().toString().matches("[A-Za-z0-9]+")) {
            f2.setError("Password must be alphanumeric");
            return false;
        }

        f1.setError(null);
        f2.setError(null);
        return true;
    }

}