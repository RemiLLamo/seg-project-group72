package com.github.remilamoureux.seg_project_group72.data;

public class EventType implements Listable {

    private String _name;
    private String _description;
    private String _id;

    public EventType(String name, String description, String id) {
        this._name = name;
        this._description = description;
        this._id = id;
    }
    public String getName() { return _name; }
    public String getDescription() { return _description; }
    public String getID() { return _id; }

    @Override
    public String getItemName() {
        return _name;
    }

    @Override
    public String getItemDesc() {
        return _description;
    }

    @Override
    public String toString() {
        return _name;
    }
}
