package com.github.remilamoureux.seg_project_group72.data;

import com.github.remilamoureux.seg_project_group72.LoggedInParticipant;
import com.github.remilamoureux.seg_project_group72.data.accounttypes.Club;

import java.util.List;

public class Event implements Listable {

    private String name;
    private String description;
    private EventType type;
    private String id;
    private List<Ejoin> participants;
    public Event(String name, String description, EventType type, String id, List<Ejoin> participants) {
        this.participants = participants;
        this.name = name;
        this.description = description;
        this.id = id;
        if (type != null) {
            this.type = type;
        } else this.type = new EventType("Invalid Event", "This event type has been removed by the admin.", "AAA");
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getTypeName() { return type.getName(); }
    public EventType getType() { return type; }
    public String getTypeDescription() { return type.getDescription(); }
    public String getID() { return id; }

    /*

        0 -> most restrictive;
        1 -> type can be anything;
        2 -> club can be anything;
        3 -> any club or type;
        4 -> any name;
        5 -> club specific;
        6 -> type specific;
        7 -> see all;

     */
    @Override
    public String getItemName() {
        LoggedInParticipant lip = DatabaseHandler.getHandler().getUpdater3();
        if (lip == null) return name + " : " + type.getName();
        int mode = lip.getMode();
        return (mode == 0 ? name : name + " : " + type.getName());
    }

    @Override
    public String getItemDesc() {
        LoggedInParticipant lip = DatabaseHandler.getHandler().getUpdater3();
        if (lip == null) return description;
        int mode = lip.getMode();
        return (mode == 0 ? description : "");
    }

    public List<Ejoin> getParticipants() {
        return participants;
    }

    public void removePart(Ejoin part) {
        this.participants.remove(part);
    }
}
