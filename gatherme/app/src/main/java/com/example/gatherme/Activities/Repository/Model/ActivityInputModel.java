package com.example.gatherme.Activities.Repository.Model;

import java.util.ArrayList;
import java.util.List;

public class ActivityInputModel {
    private String username;
    private String name;
    private String password;
    private String email;
    private String picture;
    private String description;
    private String gender;
    private int age;
    private String city;
    private String[] likes;
    private int[] communities;
    private int[] activities;
    private String[] gathers;

    public ActivityInputModel(String username, String name, String password, String email, String gender, int age, String city) {
        this.username = username;
        this.name = name;
        this.password = password;
        this.email = email;
        this.picture = "https://www.rogowaylaw.com/wp-content/uploads/Blank-Employee.jpg";
        this.description = "";
        this.gender = gender;
        this.age = age;
        this.city = city;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<String> getLikes() {
        List<String> auxList = new ArrayList<>();
        for (String like :
                this.likes) {
            auxList.add(like);
        }
        return auxList;
    }

    public void setLikes(String[] likes) {
        this.likes = likes;
    }

    public List<Integer> getCommunities() {
        List<Integer> auxList = new ArrayList<>();
        for (int community :
                this.communities) {
            auxList.add(community);
        }
        return auxList;
    }

    public void setCommunities(int[] communities) {
        this.communities = communities;
    }

    public List<Integer> getActivities() {
        List<Integer> auxList = new ArrayList<>();
        for (int activity :
                this.activities) {
            auxList.add(activity);
        }
        return auxList;
    }

    public void setActivities(int[] activities) {
        this.activities = activities;
    }

    public List<String> getGathers() {
        List<String> auxList = new ArrayList<>();
        for (String gather :
                this.gathers) {
            auxList.add(gather);
        }
        return auxList;
    }

    public void setGathers(String[] gathers) {
        this.gathers = gathers;
    }

    public void setBasicLikes() {
        String[] aux = {"empty"};
        setLikes(aux);
    }

    public void setBasicCommunities() {
        int[] aux = {-1};
        setCommunities(aux);
    }

    public void setBasicActivities() {
        int[] aux = {-1};
        setActivities(aux);
    }

    public void setBasicGathers() {
        String[] aux = {"empty"};
        setGathers(aux);
    }
}
