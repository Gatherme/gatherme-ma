package com.example.gatherme.Authentication.ViewModel;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.GetUserByEmailQuery;
import com.example.GetUsersQuery;
import com.example.TokenAuthMutation;
import com.example.UserSingInMutation;
import com.example.gatherme.Authentication.Repository.Repositories.AuthRepository;
import com.example.gatherme.Authentication.Repository.Model.UserAuth;
import com.example.gatherme.Data.API.ApolloConnector;
import com.example.gatherme.Data.Database.SharedPreferencesCon;
import com.example.gatherme.Enums.FieldStatus;
import com.example.gatherme.Enums.Reference;
import com.example.gatherme.MainActivity;
import com.example.gatherme.UserFeed.View.Activities.FeedActivity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LoginViewModel extends ViewModel {
    private UserAuth user;
    private Context ctx;
    private static final String TAG = "LoginActivityViewModel";

    public void setCtx(Context ctx) {
        this.ctx = ctx;
    }

    public UserAuth getUser() {
        return user;
    }


    public FieldStatus validateEmailField(String email) {
        // return email.contains("@");
        if (isTextEmpty(email)) {
            return FieldStatus.EMPTY_FIELD;
        } else if (!email.contains("@")) {
            return FieldStatus.EMAIL_FORMAT_ERROR;
        } else {
            return FieldStatus.OK;
        }
    }

    public FieldStatus validatePasswordField(String password) {
        if (isTextEmpty(password)) {
            return FieldStatus.EMPTY_FIELD;
        } else {
            return FieldStatus.OK;
        }
    }

    public void setUser(UserAuth user) {
        this.user = user;
    }

    public boolean isTextEmpty(String text) {
        return text.isEmpty();
    }

    public void getAndSaveUsersByEmail() {

        AuthRepository.userByEmail(user.getEmail(), new ApolloCall.Callback<GetUserByEmailQuery.Data>() {
            @Override
            public void onResponse(@Nullable Response<GetUserByEmailQuery.Data> response) {
                if (response.getData() != null) {
                    Log.d(TAG, "email: " + response.getData().userByEmail().email());
                    SharedPreferencesCon.save(ctx, Reference.EMAIL, response.getData().userByEmail().email());
                    SharedPreferencesCon.save(ctx, Reference.ID, response.getData().userByEmail().id());
                    SharedPreferencesCon.save(ctx, Reference.USERNAME, response.getData().userByEmail().username());
                } else {
                    Log.e(TAG, "email null??? NANI");
                }

            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                Log.d(TAG, "Exception " + e.getMessage(), e);
            }
        });
    }


    public void singIn(ApolloCall.Callback<UserSingInMutation.Data> callback) {
        AuthRepository.userSingIn(user.getEmail(), user.getPassword(), new ApolloCall.Callback<UserSingInMutation.Data>() {
            @Override
            public void onResponse(@Nullable Response<UserSingInMutation.Data> response) {
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
        ctx.startActivity(intent);
    }

    public void validateToken(ApolloCall.Callback<TokenAuthMutation.Data> callback) {
        String token = SharedPreferencesCon.read(ctx, Reference.TOKEN);
        AuthRepository.tokenAuth(token, new ApolloCall.Callback<TokenAuthMutation.Data>() {
            @Override
            public void onResponse(@NotNull Response<TokenAuthMutation.Data> response) {
                callback.onResponse(response);
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                callback.onFailure(e);
            }
        });
    }


}
