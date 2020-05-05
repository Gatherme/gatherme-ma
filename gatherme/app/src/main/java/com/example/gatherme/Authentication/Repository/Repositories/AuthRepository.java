package com.example.gatherme.Authentication.Repository.Repositories;

import android.util.Log;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.GetUserByEmailQuery;
import com.example.UserSingInMutation;
import com.example.gatherme.Data.API.ApolloConnector;

import org.jetbrains.annotations.NotNull;


public class AuthRepository {
    private static final String TAG = "LoginActivityRepository";

    public static void userByEmail(String email, ApolloCall.Callback<GetUserByEmailQuery.Data> callback) {
        ApolloConnector.setupApollo().query(
                GetUserByEmailQuery
                        .builder()
                        .email(email)
                        .build())
                .enqueue(new ApolloCall.Callback<GetUserByEmailQuery.Data>() {

                    @Override
                    public void onResponse(@NotNull Response<GetUserByEmailQuery.Data> response) {
                        if (response.getData() == null) {
                            Log.d(TAG, "null");
                        } else {
                            Log.d(TAG, response.getData().toString());
                            Log.d(TAG, "Email: " + response.getData().userByEmail().email());

                        }
                        callback.onResponse(response);
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {
                        Log.d(TAG, "Exception " + e.getMessage(), e);
                        callback.onFailure(e);
                    }
                });
    }

    public static void userSingIn(String email, String password, ApolloCall.Callback<UserSingInMutation.Data> callback) {
        UserSingInMutation userSingInMutation = UserSingInMutation.builder().email(email).password(password).build();
        ApolloConnector.setupApollo().mutate(userSingInMutation).enqueue(
                new ApolloCall.Callback<UserSingInMutation.Data>() {
                    @Override
                    public void onResponse(@NotNull Response<UserSingInMutation.Data> response) {
                        callback.onResponse(response);
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {
                        callback.onFailure(e);
                    }
                }
        );
    }


}
