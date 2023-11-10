package com.github.remilamoureux.seg_project_group72.data;

public abstract class Account implements Listable {

    private String username;
    private String password;
    private String email;

    public Account(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public boolean checkPassword(String ve) {
        return ve.equals(password);
    }

    abstract public String getRoleName();

    @Override
    public String getItemName() {
        return username;
    }

    @Override
    public String getItemDesc() {
        return email;
    }
}
