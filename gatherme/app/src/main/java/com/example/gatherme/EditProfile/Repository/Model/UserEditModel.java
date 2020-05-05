package com.example.gatherme.EditProfile.Repository.Model;

import java.util.ArrayList;
import java.util.List;

public class UserEditModel {
    private String id;
    private String username;
    private String name;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String[] transformerString(List<String> list){
        String [] array = new String[list.size()];
        for (int i = 0; i < list.size(); i++){
            array[i] = list.get(i);
        }
        return array;
    }

    public int[] transformerInt(List<Integer> list){
        int [] array = new int[list.size()];
        for (int i = 0; i < list.size(); i++){
            array[i] = list.get(i);
        }
        return array;
    }
}
