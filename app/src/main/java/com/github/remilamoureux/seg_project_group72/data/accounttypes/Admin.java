package com.github.remilamoureux.seg_project_group72.data.accounttypes;

import com.github.remilamoureux.seg_project_group72.data.Account;

public class Admin extends Account {
    public Admin(String username, String password, String email) {
        super(username, password, email);
    }

    @Override
    public String getRoleName() {
        return "Admin";
    }
}
