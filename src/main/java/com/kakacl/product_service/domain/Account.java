package com.kakacl.product_service.domain;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

public class Account {

    private String id;
    @NotNull
    private String user_name;
    @NotBlank
    private String roleid;

    public String getRoleid() {
        return roleid;
    }

    public void setRoleid(String roleid) {
        this.roleid = roleid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
