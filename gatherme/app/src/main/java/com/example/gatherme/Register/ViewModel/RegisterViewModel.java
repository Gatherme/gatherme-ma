package com.example.gatherme.Register.ViewModel;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.ExistUsernameQuery;
import com.example.GetUserByEmailQuery;
import com.example.gatherme.Enums.FieldStatus;
import com.example.gatherme.Register.Repository.RegisterRepository;
import com.example.gatherme.Register.Repository.UserRegisterModel;
import com.type.Register;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RegisterViewModel extends ViewModel {
    private UserRegisterModel user;
    private Context ctx;
    private static final String TAG = "RegisterViewModel";

    public UserRegisterModel getUser() {
        return user;
    }

    public void setUser(UserRegisterModel user) {
        this.user = user;
    }

    public Context getCtx() {
        return ctx;
    }

    public void setCtx(Context ctx) {
        this.ctx = ctx;
    }

    public static String getTAG() {
        return TAG;
    }

    public boolean isTextEmpty(String text) {
        return text.isEmpty();
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

    public FieldStatus validateField(String text) {
        if (isTextEmpty(text)) {
            return FieldStatus.EMPTY_FIELD;
        } else {
            return FieldStatus.OK;
        }
    }

    public FieldStatus validateUsername(String text) {
        if (isTextEmpty(text)) {
            return FieldStatus.EMPTY_FIELD;
        } else {
            return FieldStatus.OK;
        }
    }

    public void registerUser() {
        Register registerUser = Register.builder()
                .username(user.getUsername())
                .name(user.getName())
                .password(user.getPassword())
                .email(user.getEmail())
                .picture(user.getPicture())
                .description(user.getDescription())
                .gender(user.getGender())
                .age(user.getAge())
                .city(user.getCity())
                .likes(user.getLikes())
                .communities(user.getCommunities())
                .activities(user.getActivities())
                .gathers(user.getGathers()).build();
        RegisterRepository.registerUser(registerUser, new ApolloCall.Callback() {
            @Override
            public void onResponse(@NotNull Response response) {

            }

            @Override
            public void onFailure(@NotNull ApolloException e) {

            }
        });

    }

    public void existEmail(ApolloCall.Callback<GetUserByEmailQuery.Data> callback) {
        RegisterRepository.userByEmail(user.getEmail(), new ApolloCall.Callback<GetUserByEmailQuery.Data>() {
            @Override
            public void onResponse(@Nullable Response<GetUserByEmailQuery.Data> response) {
                callback.onResponse(response);

            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                callback.onFailure(e);
            }
        });
    }

    public void existUsername(ApolloCall.Callback<ExistUsernameQuery.Data> callback) {
        RegisterRepository.userByUsername(user.getUsername(), new ApolloCall.Callback<ExistUsernameQuery.Data>() {
            @Override
            public void onResponse(@Nullable Response<ExistUsernameQuery.Data> response) {
                callback.onResponse(response);
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                callback.onFailure(e);
            }
        });
    }
}
