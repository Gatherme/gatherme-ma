package com.example.gatherme.UserFeed.ViewModel;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.GetUserByIdQuery;
import com.example.gatherme.Data.Database.SharedPreferencesCon;
import com.example.gatherme.EditProfile.Repository.Repositories.EditProfileRepository;
import com.example.gatherme.Enums.Reference;
import com.example.gatherme.UserFeed.Repository.Model.UserFeedModel;

import org.jetbrains.annotations.NotNull;

public class ProfileViewModel extends ViewModel {
    private static final String TAG = "ProfileViewModel";
    // TODO: Implement the ViewModel
    private UserFeedModel user;
    private Context ctx;

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

    public void userInfo(ApolloCall.Callback<GetUserByIdQuery.Data>callback){
        String id = SharedPreferencesCon.read(ctx, Reference.ID);
        EditProfileRepository.userById(id, new ApolloCall.Callback<GetUserByIdQuery.Data>() {
            @Override
            public void onResponse(@NotNull Response<GetUserByIdQuery.Data> response) {
                user = new UserFeedModel();
                user.setUsername(response.getData().userById().username());
                user.setName(response.getData().userById().name());
                user.setPicture(response.getData().userById().picture());
                user.setDescription(response.getData().userById().description());
                callback.onResponse(response);
            }
            @Override
            public void onFailure(@NotNull ApolloException e) {
                Log.e(TAG,e.getMessage());
            }
        });
    }
}
