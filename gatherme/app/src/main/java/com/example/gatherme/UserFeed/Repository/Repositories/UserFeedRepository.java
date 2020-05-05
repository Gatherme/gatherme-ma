package com.example.gatherme.UserFeed.Repository.Repositories;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
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
}
