package com.github.remilamoureux.seg_project_group72;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import com.github.remilamoureux.seg_project_group72.data.accounttypes.Club;

import java.util.List;

public class LoggedInClub extends AppCompatActivity {

    EditText editTextName;
    EditText editTextDesc;
    TextView typeDesc;
    Button addButton;

    List<EventType> eventTypes;
    EventType curType;
    List<Event> events;
    ListView listEvents;
    AutoCompleteTextView typeMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in_club);

        editTextName = findViewById(R.id.edit_event_name);
        editTextDesc = findViewById(R.id.edit_event_desc);
        typeDesc = findViewById(R.id.event_type_desc);
        addButton = findViewById(R.id.addEventButton);
        listEvents = findViewById(R.id.eventList);
        typeMenu = findViewById(R.id.event_type);

        ArrayAdapter<EventType> test = new ListAdapter<>(this, eventTypes);
        typeMenu.setAdapter(test);

        typeMenu.setOnItemClickListener((parent, view, position, id) -> {
            curType = eventTypes.get(position);
        });

        DatabaseHandler.getHandler().setUpdater2(this);

        listEvents.setOnItemClickListener((parent, view, position, id) -> {
            Event event = events.get(position);
            openEventUpdateDialog(event.getID(), event.getTypeName());
        });
        addButton.setOnClickListener(view -> addEvent());

    }

    private void addEvent() {
        String name = editTextName.getText().toString().trim();
        String desc = editTextDesc.getText().toString().trim();
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(desc)) {
            Toast.makeText(this, "Please enter name and description", Toast.LENGTH_LONG).show();
            return;
        }
        if (curType == null) {
            Toast.makeText(this, "Please select an event type.", Toast.LENGTH_LONG).show();
            return;
        }
        DatabaseHandler.getHandler().addEvent(name, desc, curType);
        Toast.makeText(this, "Event Type added", Toast.LENGTH_LONG).show();
    }

    private void openEventUpdateDialog(String id, String typeName) {
        AlertDialog.Builder updateDialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = getLayoutInflater();
        View updateDialogView = layoutInflater.inflate(R.layout.update_dialog_event, null);
        AlertDialog updateDialog = updateDialogBuilder.create();
        updateDialog.setView(updateDialogView);

        TextView type = updateDialogView.findViewById(R.id.eventType);
        EditText name = updateDialogView.findViewById(R.id.editEventName);
        EditText desc = updateDialogView.findViewById(R.id.editEventDescription);
        Button updatebutton = updateDialogView.findViewById(R.id.buttonUpdateItem);
        Button deletebutton = updateDialogView.findViewById(R.id.buttonDeleteItem);

        type.setText(typeName);

        updateDialog.show();

        updatebutton.setOnClickListener(view -> {
            String nameS = name.getText().toString().trim();
            String descS = desc.getText().toString().trim();
            if (TextUtils.isEmpty(nameS) || TextUtils.isEmpty(descS)) {
                Toast.makeText(this, "Please enter name and description", Toast.LENGTH_LONG).show();
                return;
            }
            DatabaseHandler.getHandler().updateEvent(id, nameS, descS);
            Toast.makeText(this, "Event Type Updated", Toast.LENGTH_LONG).show();
            updateDialog.dismiss();
        });
        deletebutton.setOnClickListener(view -> {
            DatabaseHandler.getHandler().deleteEvent(id);
            Toast.makeText(this, "Event Type Deleted", Toast.LENGTH_LONG).show();
            updateDialog.dismiss();
        });
    }

    public void update() {
        Account t = DatabaseHandler.getHandler().getAccount();
        if (t instanceof Club) {
            Club t1 = (Club) t;
            events = t1.getEvents();
        } else finish();
        ListAdapter<Event> listAdapter1 = new ListAdapter<>(this, events);
        listEvents.setAdapter(listAdapter1);
    }

    public void update2(List<EventType> eventTypeList) {
        this.eventTypes = eventTypeList;
        ListAdapter<EventType> listAdapter1 = new ListAdapter<>(this, eventTypes);
        typeMenu.setAdapter(listAdapter1);
    }
}