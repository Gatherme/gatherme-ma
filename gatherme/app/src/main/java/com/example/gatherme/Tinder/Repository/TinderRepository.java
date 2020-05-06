package com.example.gatherme.Tinder.Repository;

import android.util.Log;
import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.CreateRequestMutation;
import com.example.CreateSuggestMutation;
import com.example.GetSuggestionQuery;
import com.example.UserByUsernameQuery;
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
                            Log.e(TAG, "getSuggestion null");
                        } else {
                            Log.d(TAG, "getSuggestion + " + response.getData().toString());
                        }
                        callback.onResponse(response);
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {
                        Log.e(TAG, "getSuggestion Exception " + e.getMessage(), e);
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
                            Log.e(TAG, "userByUsername is null");
                        } else {
                            Log.d(TAG, "userByUsername: " + response.getData().toString());
                        }
                        callback.onResponse(response);
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {
                        Log.e(TAG, "userByUsername Exception " + e.getMessage(), e);
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
                    Log.d(TAG, "createSuggestion: " + response.getData().toString());
                    callback.onResponse(response);
                }

                @Override
                public void onFailure(@NotNull ApolloException e) {
                    Log.e(TAG, "createSuggestion Exception " + e.getMessage(), e);
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
                    Log.d(TAG, "createRequest: " + response.getData().toString());
                    callback.onResponse(response);
                }

                @Override
                public void onFailure(@NotNull ApolloException e) {
                    Log.e(TAG, "createRequest Exception " + e.getMessage(), e);
                    callback.onFailure(e);
                }
            }
        );
    }

}
