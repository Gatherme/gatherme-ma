package com.example.gatherme.EditProfile.ViewModel;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.GetUserByIdQuery;
import com.example.UpdateUserMutation;
import com.example.gatherme.Data.Database.SharedPreferencesCon;
import com.example.gatherme.EditProfile.Repository.Model.UserEditModel;
import com.example.gatherme.EditProfile.Repository.Repositories.EditProfileRepository;
import com.example.gatherme.Enums.Reference;
import com.example.gatherme.UserFeed.View.Activities.FeedActivity;
import com.type.UserInputUpdate;

import org.jetbrains.annotations.NotNull;


public class EditDescriptionViewModel extends ViewModel {
    private static final String TAG = "EditDescriptionViewMod";
    private UserEditModel user;
    private Context ctx;

    public UserEditModel getUser() {
        return user;
    }

    public void setUser(UserEditModel user) {
        this.user = user;
    }

    public Context getCtx() {
        return ctx;
    }

    public void setCtx(Context ctx) {
        this.ctx = ctx;
    }

    public void getUserInfo(ApolloCall.Callback<GetUserByIdQuery.Data> callback) {
        setUser(new UserEditModel());
        String id = SharedPreferencesCon.read(ctx, Reference.ID);
        user.setId(id);
        Log.d(TAG, "======>id:" + id);
        EditProfileRepository.userById(id, new ApolloCall.Callback<GetUserByIdQuery.Data>() {
            @Override
            public void onResponse(@NotNull Response<GetUserByIdQuery.Data> response) {
                callback.onResponse(response);
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                callback.onFailure(e);
            }
        });
    }

    public void updateUserData(ApolloCall.Callback<UpdateUserMutation.Data> callback) {
        UserInputUpdate userInputUpdate = UserInputUpdate.builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .email(user.getEmail())
                .picture(user.getPicture())
                .description(user.getDescription())
                .gender(user.getGender())
                .age(user.getAge())
                .city(user.getCity())
                .likes(user.getLikes())
                .communities(user.getCommunities())
                .activities(user.getActivities())
                .gathers(user.getGathers()).build();
        EditProfileRepository.updateUser(userInputUpdate, new ApolloCall.Callback<UpdateUserMutation.Data>() {
            @Override
            public void onResponse(@NotNull Response<UpdateUserMutation.Data> response) {
                callback.onResponse(response);
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                callback.onFailure(e);
            }
        });
    }

    public void toHome() {
        Intent intent = new Intent(ctx, FeedActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);
    }

}
