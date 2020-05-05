package com.example.gatherme.Register.ViewModel;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.GetUserByEmailQuery;
import com.example.GetUserByUsernameQuery;
import com.example.RegisterUserMutation;
import com.example.gatherme.Data.Database.SharedPreferencesCon;
import com.example.gatherme.EditProfile.View.Activities.EditDescriptionActivity;
import com.example.gatherme.Enums.FieldStatus;
import com.example.gatherme.Enums.Reference;
import com.example.gatherme.Register.Repository.Repositories.RegisterRepository;
import com.example.gatherme.Register.Repository.Model.UserRegisterModel;
import com.example.gatherme.UserFeed.View.Activities.FeedActivity;
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

    public void registerUser(ApolloCall.Callback<RegisterUserMutation.Data> callback) {
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
        RegisterRepository.registerUser(registerUser, new ApolloCall.Callback<RegisterUserMutation.Data>() {
            @Override
            public void onResponse(@NotNull Response<RegisterUserMutation.Data> response) {
                try {
                    //Save data

                    SharedPreferencesCon.save(ctx, Reference.ID, response.getData().register().id());
                    SharedPreferencesCon.save(ctx, Reference.TOKEN, response.getData().register().token());
                    SharedPreferencesCon.save(ctx, Reference.EMAIL, user.getEmail());
                    SharedPreferencesCon.save(ctx, Reference.USERNAME, user.getUsername());

                    Log.d(TAG, response.getData().toString());
                    Log.i(TAG, "User was register");

                    callback.onResponse(response);
                } catch (NullPointerException e) {
                    Log.e(TAG, e.getMessage());
                }

            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                Log.e(TAG, e.getMessage());
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

    public void existUsername(ApolloCall.Callback<GetUserByUsernameQuery.Data> callback) {
        RegisterRepository.userByUsername(user.getUsername(), new ApolloCall.Callback<GetUserByUsernameQuery.Data>() {
            @Override
            public void onResponse(@Nullable Response<GetUserByUsernameQuery.Data> response) {
                callback.onResponse(response);
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                callback.onFailure(e);
            }
        });
    }

    public void toDescription() {
        Intent intent = new Intent(ctx, EditDescriptionActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);
    }
}
