package com.github.remilamoureux.seg_project_group72;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
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
import com.github.remilamoureux.seg_project_group72.data.accounttypes.Participant;

import java.util.ArrayList;
import java.util.List;

public class LoggedInParticipant extends AppCompatActivity {


    EditText editTextName;
    Button searchButton;
    List<EventType> eventTypes;
    List<Event> events;
    List<Club> clubs;
    ListView listEvents;
    AutoCompleteTextView typeMenu;
    AutoCompleteTextView clubMenu;
    EventType curType;
    Club curClub;
    int mode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in_participant);

        editTextName = findViewById(R.id.edit_event_name2);
        searchButton = findViewById(R.id.button);
        listEvents = findViewById(R.id.eventList);
        typeMenu = findViewById(R.id.event_type);
        clubMenu = findViewById(R.id.event_type2);

        mode = 1;

        eventTypes = new ArrayList<>();
        clubs = new ArrayList<>();
        events = new ArrayList<>();

        ArrayAdapter<EventType> test = new ListAdapter<>(this, eventTypes);
        typeMenu.setAdapter(test);
        ArrayAdapter<Club> test2 = new ListAdapter<>(this, clubs);
        clubMenu.setAdapter(test2);

        typeMenu.setOnItemClickListener((parent, view, position, id) -> {
            if (position == 0) curType = null;
            else curType = eventTypes.get(position);
            search();
        });
        clubMenu.setOnItemClickListener((parent, view, position, id) -> {
            if (position == 0) curClub = null;
            else curClub = clubs.get(position);
            search();
        });


        DatabaseHandler.getHandler().setUpdater3(this);

        listEvents.setOnItemClickListener((parent, view, position, id) -> {
            Event event = events.get(position);
            openEventUpdateDialog(event);
        });
        searchButton.setOnClickListener(view -> search());

    }

    private void search() {
        String searchTerm = editTextName.getText().toString().trim();
        mode = 0;
        if (curType == null) mode = 1;

        List<Event> goodEvents = new ArrayList<>();

        if (curClub != null) {
            for (Event e : curClub.getEvents()) {
                if (curType == null  || e.getTypeName().equals(curType.getName())) {
                    if (e.getName().contains(searchTerm)) goodEvents.add(e);
                }
            }
        } else {
            for (Club club : clubs) {
                for (Event e : club.getEvents()) {
                    if (curType == null  || e.getTypeName().equals(curType.getName())) {
                        if (e.getName().contains(searchTerm)) goodEvents.add(e);
                    }
                }
            }
        }

        events = goodEvents;
        ListAdapter<Event> listAdapter1 = new ListAdapter<>(this, goodEvents);
        listEvents.setAdapter(listAdapter1);
    }

    private void openEventUpdateDialog(Event event) {
        AlertDialog.Builder updateDialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = getLayoutInflater();
        View updateDialogView = layoutInflater.inflate(R.layout.update_dialog_join, null);
        AlertDialog updateDialog = updateDialogBuilder.create();
        updateDialog.setView(updateDialogView);

        TextView desc = updateDialogView.findViewById(R.id.textView3);
        EditText info = updateDialogView.findViewById(R.id.editTypeDescription);
        Button cancelbutton = updateDialogView.findViewById(R.id.buttonUpdateItem);
        Button joinbutton = updateDialogView.findViewById(R.id.buttonDeleteItem);

        desc.setText(event.getDescription());
        updateDialog.show();

        joinbutton.setOnClickListener(view -> {
            String descS = info.getText().toString().trim();
            if (TextUtils.isEmpty(descS)) {
                Toast.makeText(this, "Please enter information", Toast.LENGTH_LONG).show();
                return;
            }
            DatabaseHandler.getHandler().updateEventPart(event.getID(), descS);
            Toast.makeText(this, "Event Joined", Toast.LENGTH_LONG).show();
            updateDialog.dismiss();
        });
        cancelbutton.setOnClickListener(view -> {
            Toast.makeText(this, "Canceled joining event.", Toast.LENGTH_LONG).show();
            updateDialog.dismiss();
        });
    }





    public void update(List<Account> accounts) {
        Account t = DatabaseHandler.getHandler().getAccount();
        if (!(t instanceof Participant)) finish();

        events.clear();
        clubs.clear();
        clubs.add(new Club("", "AAAA", "", new ArrayList<>(), "", ""));

        for (Account acc : accounts) {
            if (acc instanceof Club) {
                events.addAll(((Club) acc).getEvents());
                clubs.add((Club) acc);
            }
        }


        ListAdapter<Event> listAdapter1 = new ListAdapter<>(this, events);
        listEvents.setAdapter(listAdapter1);
        ListAdapter<Club> listAdapter2 = new ListAdapter(this, clubs);
        clubMenu.setAdapter(listAdapter2);
    }

    public void update2(List<EventType> eventTypeList) {
        this.eventTypes = new ArrayList<>();
        for (EventType e : eventTypeList) {
            eventTypes.add(e);
        }
        eventTypes.add(0, new EventType("", "", "AAAA"));
        ListAdapter<EventType> listAdapter1 = new ListAdapter(this, eventTypes);
        typeMenu.setAdapter(listAdapter1);
    }

    public int getMode() {
        return mode;
    }
}