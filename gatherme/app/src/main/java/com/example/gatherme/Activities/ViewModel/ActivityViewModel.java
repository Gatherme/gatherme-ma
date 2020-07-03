package com.example.gatherme.Activities.ViewModel;

import android.content.Context;
import android.util.Log;
import androidx.lifecycle.ViewModel;
import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.CreateRequestMutation;
import com.example.CreateSuggestMutation;
import com.example.GetSuggestionQuery;
import com.example.UserByUsernameQuery;
import com.example.gatherme.Tinder.Repository.TinderRepository;
import org.jetbrains.annotations.NotNull;

public class TinderViewModel extends ViewModel {
    private static final String TAG = "TinderViewModel";

    private String id_user;
    private String username;
    private String user_destination;
    private String token;
    private Context ctx;

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUser_destination() {
        return user_destination;
    }

    public void setUser_destination(String user_destination) {
        this.user_destination = user_destination;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Context getCtx() {
        return ctx;
    }

    public void setCtx(Context ctx) {
        this.ctx = ctx;
    }


    public void getSuggestion(ApolloCall.Callback<GetSuggestionQuery.Data> callback) {
        TinderRepository.getSuggestion(id_user, new ApolloCall.Callback<GetSuggestionQuery.Data>(){

            @Override
            public void onResponse(@NotNull Response<GetSuggestionQuery.Data> response) {
                if (response.getData() != null) {
                    Log.d(TAG, "getSuggestion: " + response.getData().getSuggestion().toString());

                }else{
                    Log.e(TAG, "getSuggestion is null");
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


    public void userByUsername(ApolloCall.Callback<UserByUsernameQuery.Data> callback){
        TinderRepository.userByUsername(username, new ApolloCall.Callback<UserByUsernameQuery.Data>() {

            @Override
            public void onResponse(@NotNull Response<UserByUsernameQuery.Data> response) {
                if (response.getData() != null) {
                    Log.d(TAG, "userByUsername: " + response.getData().userByUsername().toString());

                }else{
                    Log.e(TAG, "userByUsername is null");
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


    public void createSuggestion(ApolloCall.Callback<CreateSuggestMutation.Data> callback){
        TinderRepository.createSuggestion(id_user, new ApolloCall.Callback<CreateSuggestMutation.Data>() {

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
        }) ;
    }


    public void createRequest(ApolloCall.Callback<CreateRequestMutation.Data> callback){
        TinderRepository.createRequest(id_user, user_destination, token, new ApolloCall.Callback<CreateRequestMutation.Data>() {

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
        });
    }

}
