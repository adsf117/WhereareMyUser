package com.andres.wherearemyuser.dataobjects;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by andresdavid on 16/09/16.
 */
public class User extends RealmObject {

    @PrimaryKey
    private long id;
    private String name;
    private String username;
    private String email;
    private String phone;
    private String website;
    private Address address;

    public User() {
    }

    public User(long id, String name, String username, String email, String phone, String website, Address address) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.website = website;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
