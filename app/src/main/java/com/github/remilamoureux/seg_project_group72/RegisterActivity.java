package com.github.remilamoureux.seg_project_group72;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.github.remilamoureux.seg_project_group72.data.DatabaseHandler;

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

        String email = ((EditText)findViewById(R.id.phoneField)).getText().toString();
        String usr = ((EditText)findViewById(R.id.usernameField)).getText().toString();
        String pwd = ((EditText)findViewById(R.id.passwordField)).getText().toString();
        boolean role = ((Switch)findViewById(R.id.switch1)).isChecked();

        DatabaseHandler.getHandler().attemptCreateAccount(usr, pwd, email, role);

        Intent intent = getIntent();
        intent.putExtra("username", usr);
        intent.putExtra("password", pwd);
        setResult(RESULT_OK, intent);
        finish();
    }

    private boolean validateFields() {
        EditText f1 = findViewById(R.id.usernameField);
        EditText f2 = findViewById(R.id.passwordField);
        EditText f3 = findViewById(R.id.phoneField);

        if (f1.length() == 0) {
            f1.setError("Must have username");
            return false;
        }
        if (f2.length() == 0) {
            f2.setError("Must have password");
            return false;
        }
        if (f3.length() == 0) {
            f3.setError("Must have email");
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

        if (!f3.getText().toString().matches("([a-zA-Z0-9_-]+)@([a-zA-Z0-9_-]+).([a-zA-Z]{2,5})$")) {
            f3.setError("Not a valid email address.");
            return false;
        }

        f1.setError(null);
        f2.setError(null);
        f3.setError(null);
        return true;
    }
}