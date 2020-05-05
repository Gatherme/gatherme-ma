package com.example.gatherme.UserFeed.ViewModel;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.TokenOutMutation;
import com.example.gatherme.Authentication.View.Activities.LoginActivity;
import com.example.gatherme.Data.Database.SharedPreferencesCon;
import com.example.gatherme.Enums.Reference;
import com.example.gatherme.UserFeed.Repository.Model.UserFeedModel;
import com.example.gatherme.UserFeed.Repository.Repositories.UserFeedRepository;

import org.jetbrains.annotations.NotNull;

public class UserFeedViewModel extends ViewModel {
    private static final String TAG = "UserFeedViewModel";
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

    public void singOut(){
        String token = SharedPreferencesCon.read(ctx, Reference.TOKEN);
        UserFeedRepository.tokenOut(token, new ApolloCall.Callback<TokenOutMutation.Data>() {
            @Override
            public void onResponse(@NotNull Response<TokenOutMutation.Data> response) {
                goToSingIn();
                Log.i(TAG,"Sing Out");
                SharedPreferencesCon.deleteValue(ctx,Reference.PASSWORD);
                SharedPreferencesCon.deleteValue(ctx,Reference.EMAIL);
                SharedPreferencesCon.deleteValue(ctx,Reference.TOKEN);
                SharedPreferencesCon.deleteValue(ctx,Reference.USERNAME);
                SharedPreferencesCon.deleteValue(ctx,Reference.ID);
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                Log.e(TAG,e.getMessage());
            }
        });
    }

    public void goToSingIn(){
        Intent intent = new Intent(ctx, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);
    }
}
