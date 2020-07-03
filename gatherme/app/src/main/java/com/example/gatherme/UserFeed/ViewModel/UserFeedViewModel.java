package com.example.gatherme.UserFeed.ViewModel;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.GetActivitiesByCategoryQuery;
import com.example.GetActivitiesQuery;
import com.example.TokenOutMutation;
import com.example.gatherme.Authentication.View.Activities.LoginActivity;
import com.example.gatherme.Data.Database.SharedPreferencesCon;
import com.example.gatherme.Enums.Reference;
import com.example.gatherme.UserFeed.Repository.Model.ActivityFModel;
import com.example.gatherme.UserFeed.Repository.Model.UserFeedModel;
import com.example.gatherme.UserFeed.Repository.Repositories.UserFeedRepository;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

public class UserFeedViewModel extends ViewModel {
    private static final String TAG = "UserFeedViewModel";
    private UserFeedModel user;
    private Context ctx;
    private ArrayList<ActivityFModel> activityList;

    public UserFeedModel getUser() {
        return user;
    }

    public void setUser(UserFeedModel user) {
        this.user = user;
    }

    public Context getCtx() {
        return ctx;
    }

    public void setCtx(Context ctx) {
        this.ctx = ctx;
    }

    public void singOut() {
        String token = SharedPreferencesCon.read(ctx, Reference.TOKEN);
        UserFeedRepository.tokenOut(token, new ApolloCall.Callback<TokenOutMutation.Data>() {
            @Override
            public void onResponse(@NotNull Response<TokenOutMutation.Data> response) {
                goToSingIn();
                Log.i(TAG, "Sing Out");
                SharedPreferencesCon.deleteValue(ctx, Reference.PASSWORD);
                SharedPreferencesCon.deleteValue(ctx, Reference.EMAIL);
                SharedPreferencesCon.deleteValue(ctx, Reference.TOKEN);
                SharedPreferencesCon.deleteValue(ctx, Reference.USERNAME);
                SharedPreferencesCon.deleteValue(ctx, Reference.ID);
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                Log.e(TAG, e.getMessage());
            }
        });
    }

    public void getActivities(ApolloCall.Callback<GetActivitiesQuery.Data> callback) {
        activityList = new ArrayList<>();
        UserFeedRepository.getActivities(new ApolloCall.Callback<GetActivitiesQuery.Data>() {
            @Override
            public void onResponse(Response<GetActivitiesQuery.Data> response) {
                if (response.getData().getAllActivities() == null) {
                    Log.e(TAG, "activities null");
                }
                try {

                    for (GetActivitiesQuery.GetAllActivity activity : response.getData().getAllActivities()) {
                        activityList.add(new ActivityFModel(
                                activity.banner(), activity.nombre(), activity.administrador(),
                                activity.fecha(), activity.hora(), activity.id()));
                    }
                    callback.onResponse(response);

                } catch (NullPointerException e) {
                    Log.e(TAG, e.getMessage());
                }
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                callback.onFailure(e);
            }
        });
    }

    public void getActivities(String category, ApolloCall.Callback<GetActivitiesByCategoryQuery.Data> callback) {
        activityList = new ArrayList<>();
        UserFeedRepository.getActivities(category, new ApolloCall.Callback<GetActivitiesByCategoryQuery.Data>() {
            @Override
            public void onResponse(@NotNull Response<GetActivitiesByCategoryQuery.Data> response) {
                try {
                    for (GetActivitiesByCategoryQuery.GetActivitiesByCategory activity : response.getData().getActivitiesByCategory()) {
                        activityList.add(new ActivityFModel(
                                activity.banner(), activity.nombre(), activity.administrador(),
                                activity.fecha(), activity.hora(), activity.id()));
                    }
                    callback.onResponse(response);
                } catch (NullPointerException e) {
                    Log.e(TAG, e.getMessage());
                }

            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                callback.onFailure(e);
            }
        });
    }

    public void goToSingIn() {
        Intent intent = new Intent(ctx, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);
    }

    public ArrayList<ActivityFModel> getActivityList() {
        return activityList;
    }

    public void setActivityList(ArrayList<ActivityFModel> activityList) {
        this.activityList = activityList;
    }
}
