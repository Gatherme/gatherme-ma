package com.example.gatherme.Tinder.ViewModel;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.GetUserByEmailQuery;
import com.example.GetUsersQuery;
import com.example.UserSingInMutation;
import com.example.gatherme.Authentication.Repository.AuthRepository;
import com.example.gatherme.Authentication.Repository.UserAuth;
import com.example.gatherme.Data.API.ApolloConnector;
import com.example.gatherme.Enums.FieldStatus;
import com.example.gatherme.MainActivity;

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

    public void existEmail() {

        AuthRepository.userByEmail(user.getEmail(), new ApolloCall.Callback<GetUserByEmailQuery.Data>() {
            @Override
            public void onResponse(@Nullable Response<GetUserByEmailQuery.Data> response) {
                if (response.getData() != null) {
                    Log.d(TAG, "email: " + response.getData().userByEmail().email());
                }else{
                    Log.d(TAG, "null");
                }

            }
            @Override
            public void onFailure(@NotNull ApolloException e) {
                Log.d(TAG, "Exception " + e.getMessage(), e);
            }
        });
    }


    public void users() {
        ApolloConnector.setupApollo().query(
                GetUsersQuery
                        .builder()
                        .build()
        ).enqueue(new ApolloCall.Callback<GetUsersQuery.Data>() {
            @Override
            public void onResponse(@NotNull Response<GetUsersQuery.Data> response) {
                Log.d(TAG, "ans Users:" + response.getData().users().get(1).id());
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                Log.d(TAG, "**************************Exception " + e.getMessage(), e);
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
    public void toHome(){
        Intent intent = new Intent(ctx,MainActivity.class);
        ctx.startActivity(intent);
    }


}
