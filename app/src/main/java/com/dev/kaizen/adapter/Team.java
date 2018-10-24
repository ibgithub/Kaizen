package com.dev.kaizen.adapter;

import org.json.JSONObject;

public class Team {
    private int id;
    private String fullName, schoolClass, address;
    private JSONObject user, school;

    public Team() {

    }

    public Team(int id, String fullName, String schoolClass, String address,
                JSONObject user, JSONObject school) {
        this.id = id;
        this.fullName = fullName;
        this.schoolClass = schoolClass;
        this.address = address;
        this.user = user;
        this.school = school;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getSchoolClass() {
        return schoolClass;
    }

    public void setSchoolClass(String schoolClass) {
        this.schoolClass = schoolClass;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public JSONObject getUser() {
        return user;
    }

    public void setUser(JSONObject user) {
        this.user = user;
    }

    public JSONObject getSchool() {
        return school;
    }

    public void setSchool(JSONObject school) {
        this.school = school;
    }
}
