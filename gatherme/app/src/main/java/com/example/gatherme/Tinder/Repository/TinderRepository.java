package com.example.gatherme.Tinder.Repository;

import android.util.Log;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.CreateRequestMutation;
import com.example.CreateSuggestMutation;
import com.example.GetSuggestionQuery;
import com.example.GetUserByEmailQuery;
import com.example.UserByUsernameQuery;
import com.example.UserSingInMutation;
import com.example.gatherme.Data.API.ApolloConnector;

import org.jetbrains.annotations.NotNull;


public class TinderRepository {
    private static final String TAG = "TinderRepository";

    public static void getSuggestion(String id, ApolloCall.Callback<GetSuggestionQuery.Data> callback) {
        ApolloConnector.setupApollo().query(
            GetSuggestionQuery
                .builder()
                .id(id)
                .build()).enqueue(new ApolloCall.Callback<GetSuggestionQuery.Data>() {

                    @Override
                    public void onResponse(@NotNull Response<GetSuggestionQuery.Data> response) {
                        if (response.getData() == null) {
                            Log.d(TAG, "null");
                        } else {
                            Log.d(TAG, response.getData().toString());
                            Log.d(TAG, "Email: " + response.getData().getSuggestion().toString());
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

    public static void userByUsername(String username, ApolloCall.Callback<UserByUsernameQuery.Data> callback){
        ApolloConnector.setupApollo().query(
                UserByUsernameQuery
                .builder()
                .username(username)
                .build()).enqueue(new ApolloCall.Callback<UserByUsernameQuery.Data>() {
            @Override
            public void onResponse(@NotNull Response<UserByUsernameQuery.Data> response) {
                if (response.getData() == null) {
                    Log.d(TAG, "userByUsername null");
                } else {
                    Log.d(TAG, response.getData().toString());
                    Log.d(TAG, "userByUsername: " + response.getData().userByUsername().toString());
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

    public static void createSuggestion(String id, ApolloCall.Callback<CreateSuggestMutation.Data> callback){
        CreateSuggestMutation createSuggestMutation = CreateSuggestMutation.builder().id(id).build();
        ApolloConnector.setupApollo().mutate(createSuggestMutation).enqueue(
                new ApolloCall.Callback<CreateSuggestMutation.Data>() {
                    @Override
                    public void onResponse(@NotNull Response<CreateSuggestMutation.Data> response) {
                        callback.onResponse(response);
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {
                        callback.onFailure(e);
                    }
                }

        );
    }

    public static void createRequest(String user_origin, String user_destination, String token, ApolloCall.Callback<CreateRequestMutation.Data> callback){
        CreateRequestMutation createRequestMutation = CreateRequestMutation.builder().user_origin(user_origin).user_destination(user_destination).token(token).build();
        ApolloConnector.setupApollo().mutate(createRequestMutation).enqueue(
                new ApolloCall.Callback<CreateRequestMutation.Data>() {
                    @Override
                    public void onResponse(@NotNull Response<CreateRequestMutation.Data> response) {
                        Log.d(TAG, "solicitud " +response.toString());
                        callback.onResponse(response);
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {
                        Log.d(TAG, "fffffffffffffffffffffffffffffffffffffffffffffffff solicitud " +e.toString());
                        callback.onFailure(e);
                    }
                }
        );
    }
}
