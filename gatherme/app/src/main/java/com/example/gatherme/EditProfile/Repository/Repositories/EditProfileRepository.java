package com.example.gatherme.EditProfile.Repository.Repositories;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.GetUserByIdQuery;
import com.example.UpdateUserMutation;
import com.example.gatherme.Data.API.ApolloConnector;
import com.type.UserInputUpdate;

import org.jetbrains.annotations.NotNull;

public class EditProfileRepository {
    private static final String TAG = "EditProfileRepository";

    public static void updateUser(UserInputUpdate myUser, ApolloCall.Callback<UpdateUserMutation.Data> callback){
        UpdateUserMutation updateUserMutation = UpdateUserMutation.builder().user(myUser).build();
        ApolloConnector.setupApollo().mutate(updateUserMutation).enqueue(new ApolloCall.Callback<UpdateUserMutation.Data>() {
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

    public static void userById(String id, ApolloCall.Callback<GetUserByIdQuery.Data> callback){
        GetUserByIdQuery getUserByIdQuery = GetUserByIdQuery.builder().id(id).build();
        ApolloConnector.setupApollo().query(getUserByIdQuery).enqueue(new ApolloCall.Callback<GetUserByIdQuery.Data>() {
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
}
