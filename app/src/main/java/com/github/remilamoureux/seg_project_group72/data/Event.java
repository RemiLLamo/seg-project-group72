package com.github.remilamoureux.seg_project_group72.data;

public class Event implements Listable {

    private String name;
    private String description;
    private EventType type;
    private String id;

    public Event(String name, String description, EventType type, String id) {
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
    public String getTypeDescription() { return type.getDescription(); }
    public String getID() { return id; }

    @Override
    public String getItemName() {
        return name + " : " + type.getName();
    }

    @Override
    public String getItemDesc() {
        return description;
    }

}
