package com.github.remilamoureux.seg_project_group72;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.remilamoureux.seg_project_group72.data.Account;
import com.github.remilamoureux.seg_project_group72.data.DatabaseHandler;
import com.github.remilamoureux.seg_project_group72.data.Event;
import com.github.remilamoureux.seg_project_group72.data.EventType;
import com.github.remilamoureux.seg_project_group72.data.ListAdapter;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class LoggedInAdmin extends AppCompatActivity {
    ListView listAcc;
    ListView listET;

    List<Account> accounts;
    List<EventType> eventTypes;

    EditText editTextName;
    EditText editTextDesc;
    Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in_admin);

        listAcc = findViewById(R.id.listViewAccounts);
        listET = findViewById(R.id.listViewEventTypes);

        editTextName = findViewById(R.id.editEventTName);
        editTextDesc = findViewById(R.id.editEventTDesc);
        addButton = findViewById(R.id.eventTAdd);

        DatabaseHandler.getHandler().setUpdater(this);

        listAcc.setOnItemClickListener((parent, view, position, id) -> {
            Account account = accounts.get(position);
            openAccountUpdateDialog(account.getUsername());
        });
        listET.setOnItemClickListener((parent, view, position, id) -> {
            EventType eventT = eventTypes.get(position);
            openETUpdateDialog(eventT.getID());
        });

        addButton.setOnClickListener(view -> addEventType());
    }

    private void addEventType() {
        String name = editTextName.getText().toString().trim();
        String desc = editTextDesc.getText().toString().trim();
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(desc)) {
            Toast.makeText(this, "Please enter name and description", Toast.LENGTH_LONG).show();
            return;
        }
        DatabaseHandler.getHandler().addEventType(name, desc);
        Toast.makeText(this, "Event Type added", Toast.LENGTH_LONG).show();
    }

    public void update(List<Account> accounts) {
        this.accounts = accounts;
        ListAdapter<Account> listAdapter1 = new ListAdapter<>(this, accounts);
        listAcc.setAdapter(listAdapter1);
    }

    public void update2(List<EventType> eventTypes) {
        this.eventTypes = eventTypes;
        ListAdapter<EventType> listAdapter2 = new ListAdapter<>(this, eventTypes);
        listET.setAdapter(listAdapter2);
    }

    private void openAccountUpdateDialog(String usr) {
        AlertDialog.Builder updateDialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = getLayoutInflater();
        View updateDialogView = layoutInflater.inflate(R.layout.update_dialog_account, null);
        AlertDialog updateDialog = updateDialogBuilder.create();
        updateDialog.setView(updateDialogView);

        TextView name = updateDialogView.findViewById(R.id.accountName);
        Button backbutton = updateDialogView.findViewById(R.id.buttonUpdateItem);
        Button deletebutton = updateDialogView.findViewById(R.id.buttonDeleteItem);

        name.setText(usr);

        updateDialog.show();

        backbutton.setOnClickListener(view -> {
            updateDialog.dismiss();
        });

        deletebutton.setOnClickListener(view -> {
            DatabaseHandler.getHandler().deleteAccount(usr);
            Toast.makeText(this, "Account Deleted", Toast.LENGTH_LONG).show();
            updateDialog.dismiss();
        });
    }

    private void openETUpdateDialog(String id) {
        AlertDialog.Builder updateDialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = getLayoutInflater();
        View updateDialogView = layoutInflater.inflate(R.layout.update_dialog, null);
        AlertDialog updateDialog = updateDialogBuilder.create();
        updateDialog.setView(updateDialogView);

        EditText name = updateDialogView.findViewById(R.id.editTypeName);
        EditText desc = updateDialogView.findViewById(R.id.editTypeDescription);
        Button updatebutton = updateDialogView.findViewById(R.id.buttonUpdateItem);
        Button deletebutton = updateDialogView.findViewById(R.id.buttonDeleteItem);

        updateDialog.show();

        updatebutton.setOnClickListener(view -> {
            String nameS = name.getText().toString().trim();
            String descS = desc.getText().toString().trim();
            if (TextUtils.isEmpty(nameS) || TextUtils.isEmpty(descS)) {
                Toast.makeText(this, "Please enter name and description", Toast.LENGTH_LONG).show();
                return;
            }
            DatabaseHandler.getHandler().updateEventType(id, nameS, descS);
            Toast.makeText(this, "Event Type Updated", Toast.LENGTH_LONG).show();
            updateDialog.dismiss();
        });
        deletebutton.setOnClickListener(view -> {
            DatabaseHandler.getHandler().deleteEventType(id);
            Toast.makeText(this, "Event Type Deleted", Toast.LENGTH_LONG).show();
            updateDialog.dismiss();
        });

    }
}