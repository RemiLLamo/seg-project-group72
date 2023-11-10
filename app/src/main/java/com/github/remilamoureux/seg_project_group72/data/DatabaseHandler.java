package com.github.remilamoureux.seg_project_group72.data;

import androidx.annotation.NonNull;

import com.github.remilamoureux.seg_project_group72.LoggedInAdmin;
import com.github.remilamoureux.seg_project_group72.LoggedInClub;
import com.github.remilamoureux.seg_project_group72.MainActivity;
import com.github.remilamoureux.seg_project_group72.data.accounttypes.Admin;
import com.github.remilamoureux.seg_project_group72.data.accounttypes.Club;
import com.github.remilamoureux.seg_project_group72.data.accounttypes.Participant;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler {

    private static DatabaseHandler handler;
    private DatabaseHandler() {}

    private LoggedInAdmin updater;
    private LoggedInClub updater2;

    private List<Account> accounts;
    private List<EventType> eventTypes;

    private Account loggedInAcc;
    private Admin adAcc;

    public static DatabaseHandler getHandler() {
        if (handler == null) {
            handler = new DatabaseHandler();
            handler.setup();
        }

        return handler;
    }

    private void setup() {

        accounts = new ArrayList<>();
        eventTypes = new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference accountRef = database.getReference("users");
        DatabaseReference eventtypeRef = database.getReference("event_types");


        eventtypeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                eventTypes.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    eventTypes.add(new EventType(postSnapshot.child("name").getValue(String.class), postSnapshot.child("desc").getValue(String.class), postSnapshot.getKey()));
                }
                if (updater != null) updater.update2(eventTypes);
                if (updater2 != null) updater2.update2(eventTypes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        accountRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                accounts.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Account newA;
                    String role = postSnapshot.child("role").getValue(String.class);
                    if (role.equals("Admin")) {
                        newA = new Admin(postSnapshot.getKey(), postSnapshot.child("password").getValue(String.class), postSnapshot.child("email").getValue(String.class));
                    } else if (role.equals("Group")) {
                        newA = new Club(postSnapshot.getKey(), postSnapshot.child("password").getValue(String.class), postSnapshot.child("email").getValue(String.class),
                                getEvents(postSnapshot.child("events")));
                    } else {
                        newA = new Participant(postSnapshot.getKey(), postSnapshot.child("password").getValue(String.class), postSnapshot.child("email").getValue(String.class));
                    }
                    if (!(newA instanceof Admin)) accounts.add(newA);
                    else adAcc = (Admin) newA;
                }

                if (loggedInAcc != null) relog();
                if (updater != null) updater.update(accounts);
                if (updater2 != null) updater2.update();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private EventType getType(String id) {
        for (EventType type : eventTypes) {
            if (type.getID().equals(id)) return type;
        }
        return null;
    }

    private List<Event> getEvents(DataSnapshot shot) {
        List<Event> eA = new ArrayList<>();
        for (DataSnapshot cS : shot.getChildren()) {
            eA.add(new Event(cS.child("name").getValue(String.class), cS.child("desc").getValue(String.class), getType(cS.child("typeID").getValue(String.class)), cS.getKey()));
        }
        return eA;
    }

    public Account attemptLogin(String username, String password) {
        if (username.equals("admin") && password.equals("admin")) {
            loggedInAcc = adAcc;
            return adAcc;
        }
        for (Account acc : accounts) {
            if (acc.getUsername().equals(username)) {
                if (acc.checkPassword(password)) {
                    loggedInAcc = acc;
                    return acc;
                }
            }
        }
        loggedInAcc = null;
        return null;
    }

    private void relog() {
        if (loggedInAcc != null && loggedInAcc.getUsername().equals("admin")) return;
        for (Account acc : accounts) {
            if (acc.getUsername().equals(loggedInAcc.getUsername())) {
                loggedInAcc = acc;
                return;
            }
        }
        loggedInAcc = null;
    }

    public Account getAccount() { return loggedInAcc; }

    public void attemptCreateAccount(String usr, String pwd, String email, boolean role) {

        for (Account acc : accounts) {
            if (acc.getUsername().equals(usr)) return;
        }

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
    }

    public void setUpdater(LoggedInAdmin th) {
        updater = th;
        updater.update(accounts);
        updater.update2(eventTypes);
    }
    public void setUpdater2(LoggedInClub th) {
        updater2 = th;
        recache();
        updater2.update();
        updater2.update2(eventTypes);
    }

    public void addEventType(String name, String desc) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference eTypes = database.getReference("event_types");

        String id = eTypes.push().getKey();
        eTypes.child(id).child("name").setValue(name);
        eTypes.child(id).child("desc").setValue(desc);
    }

    public void updateEventType(String id, String name, String desc) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference eTypes = database.getReference("event_types");
        eTypes.child(id).child("name").setValue(name);
        eTypes.child(id).child("desc").setValue(desc);
    }

    public void deleteEventType(String id) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference eTypes = database.getReference("event_types");
        eTypes.child(id).removeValue();
    }

    public void addEvent(String name, String desc, EventType type) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users").child(loggedInAcc.getUsername()).child("events");

        String id = ref.push().getKey();
        ref.child(id).child("name").setValue(name);
        ref.child(id).child("desc").setValue(desc);
        ref.child(id).child("typeID").setValue(type != null? type.getID() : "AAA");
    }

    public void updateEvent(String id, String name, String desc) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users").child(loggedInAcc.getUsername()).child("events").child(id);
        ref.child("name").setValue(name);
        ref.child("desc").setValue(desc);
    }

    public void deleteEvent(String id) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users").child(loggedInAcc.getUsername()).child("events").child(id);
        ref.removeValue();
    }

    public void deleteAccount(String usr) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference users = database.getReference("users");
        users.child(usr).removeValue();
    }

    private void recache() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference accountRef = database.getReference("users");
        accountRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                accounts.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Account newA;
                    String role = postSnapshot.child("role").getValue(String.class);
                    if (role.equals("Admin")) {
                        newA = new Admin(postSnapshot.getKey(), postSnapshot.child("password").getValue(String.class), postSnapshot.child("email").getValue(String.class));
                    } else if (role.equals("Group")) {
                        newA = new Club(postSnapshot.getKey(), postSnapshot.child("password").getValue(String.class), postSnapshot.child("email").getValue(String.class),
                                getEvents(postSnapshot.child("events")));
                    } else {
                        newA = new Participant(postSnapshot.getKey(), postSnapshot.child("password").getValue(String.class), postSnapshot.child("email").getValue(String.class));
                    }
                    if (!(newA instanceof Admin)) accounts.add(newA);
                    else adAcc = (Admin) newA;
                }

                if (loggedInAcc != null) relog();
                if (updater != null) updater.update(accounts);
                if (updater2 != null) updater2.update();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
