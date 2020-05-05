package com.example.gatherme.Register.Repository;

import android.util.Log;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.ExistUsernameQuery;
import com.example.GetUserByEmailQuery;
import com.example.RegisterUserMutation;
import com.example.gatherme.Data.API.ApolloConnector;
import com.type.Register;

import org.jetbrains.annotations.NotNull;

public class RegisterRepository {
    private static final String TAG = "RegisterRepository";

    public static void registerUser(Register user, ApolloCall.Callback<RegisterUserMutation.Data> callback) {
        RegisterUserMutation registerUserMutation = RegisterUserMutation.builder().user(user).build();
        ApolloConnector.setupApollo().mutate(registerUserMutation).enqueue(new ApolloCall.Callback<RegisterUserMutation.Data>() {
            @Override
            public void onResponse(@NotNull Response<RegisterUserMutation.Data> response) {
                Log.d(TAG,response.getData().toString());
                callback.onResponse(response);
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                callback.onFailure(e);
            }
        });
    }

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

    public static void userByUsername(String name, ApolloCall.Callback<ExistUsernameQuery.Data> callback) {
        //ExistUsernameQuery existUsernameQuery = ExistUsernameQuery.builder().user(name).build();
        ApolloConnector.setupApollo().query(
                ExistUsernameQuery
                        .builder()
                        .user(name)
                        .build())
                .enqueue(new ApolloCall.Callback<ExistUsernameQuery.Data>() {

                    @Override
                    public void onResponse(@NotNull Response<ExistUsernameQuery.Data> response) {
                        if (response.getData() == null) {
                            Log.d(TAG, "null");
                        } else {
                            Log.d(TAG, response.getData().toString());
                            Log.d(TAG, "Username: " + response.getData().userByUsername().username());

                        }
                        callback.onResponse(response);
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {
                        callback.onFailure(e);
                    }
                });
    }
}
