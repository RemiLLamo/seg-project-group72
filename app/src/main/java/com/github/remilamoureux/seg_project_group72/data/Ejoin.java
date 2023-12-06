package com.github.remilamoureux.seg_project_group72.data;

public class Ejoin implements Listable{

    private String name;
    private String info;

    public Ejoin(String name, String info) {
        this.name = name;
        this.info = info;
    }
    @Override
    public String getItemName() {
        return name;
    }

    @Override
    public String getItemDesc() {
        return info;
    }
}
