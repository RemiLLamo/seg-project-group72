package com.github.remilamoureux.seg_project_group72.data.accounttypes;

import com.github.remilamoureux.seg_project_group72.data.Account;
import com.github.remilamoureux.seg_project_group72.data.Event;

import java.util.List;

public class Club extends Account {

    private List<Event> events;
    public Club(String username, String password, String email, List<Event> events) {
        super(username, password, email);
        this.events = events;
    }

    public Event getEvent(int index) {
        return events.get(index);
    }

    public int getNumEvents() {
        return events.size();
    }
    @Override
    public String getRoleName() {
        return "Cycling Club";
    }

    public List<Event> getEvents() {
        return events;
    }
}
