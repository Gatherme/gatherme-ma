package com.example.gatherme.UserFeed.Repository.Model;

import com.example.gatherme.Data.Models.ActivityModel;

public class ActivityFModel extends ActivityModel {
    public ActivityFModel(String banner, String title, String admin, String date, String hour, int id) {
        this.setBanner(banner);
        this.setName(title);
        this.setAdmin(admin);
        this.setDate(date);
        this.setHour(hour);
        this.setId(id);
    }
}
