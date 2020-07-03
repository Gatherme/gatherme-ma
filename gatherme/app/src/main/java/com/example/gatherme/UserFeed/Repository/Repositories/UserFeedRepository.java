package com.example.gatherme.UserFeed.Repository.Repositories;

import android.util.Log;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.GetActivitiesByCategoryQuery;
import com.example.GetActivitiesQuery;
import com.example.TokenOutMutation;
import com.example.gatherme.Data.API.ApolloConnector;

import org.jetbrains.annotations.NotNull;

public class UserFeedRepository {

    private static final String TAG = "UserFeedRepository";


    public static void tokenOut(String token, ApolloCall.Callback<TokenOutMutation.Data> callback) {
        TokenOutMutation tokenOutMutation = TokenOutMutation.builder().token(token).build();
        ApolloConnector.setupApollo().mutate(tokenOutMutation).enqueue(
                new ApolloCall.Callback<TokenOutMutation.Data>() {
                    @Override
                    public void onResponse(@NotNull Response<TokenOutMutation.Data> response) {
                        callback.onResponse(response);
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {
                        callback.onFailure(e);
                    }
                }
        );
    }

    public static void getActivities(ApolloCall.Callback<GetActivitiesQuery.Data> callback) {
        ApolloConnector.setupApollo().query(
                GetActivitiesQuery
                        .builder()
                        .build())
                .enqueue(new ApolloCall.Callback<GetActivitiesQuery.Data>() {
                             @Override
                             public void onResponse(Response<GetActivitiesQuery.Data> response) {
                                 Log.i(TAG, "Activities");
                                 callback.onResponse(response);

                             }

                             @Override
                             public void onFailure(@NotNull ApolloException e) {
                                 Log.e(TAG, "Error with activities");
                                 callback.onFailure(e);
                             }
                         }
                );
    }

    public static void getActivities(String category, ApolloCall.Callback<GetActivitiesByCategoryQuery.Data> callback) {
        GetActivitiesByCategoryQuery getActivitiesByCategoryQuery = GetActivitiesByCategoryQuery.builder()
                .category(category)
                .build();
        ApolloConnector.setupApollo().query(getActivitiesByCategoryQuery).enqueue(
                new ApolloCall.Callback<GetActivitiesByCategoryQuery.Data>() {
                    @Override
                    public void onResponse(@NotNull Response<GetActivitiesByCategoryQuery.Data> response) {
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
