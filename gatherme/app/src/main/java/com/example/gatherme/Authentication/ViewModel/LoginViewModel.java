package com.example.gatherme.Authentication.ViewModel;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.GetUserByEmailQuery;
import com.example.GetUsersQuery;
import com.example.gatherme.Authentication.Repository.UserAuth;
import com.example.gatherme.Data.API.ApolloConnector;
import com.example.gatherme.R;
import com.example.gatherme.Register.View.Activities.RegisterActivity;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

public class LoginViewModel extends ViewModel {
    private UserAuth user;
    private TextInputLayout emailEditText;
    private TextInputLayout passwordEditText;
    private Context ctx;
    private static final String TAG = "LoginActivity";


    public void setEmailEditText(TextInputLayout emailEditText) {
        this.emailEditText = emailEditText;
    }

    public void setPasswordEditText(TextInputLayout passwordEditText) {
        this.passwordEditText = passwordEditText;
    }

    public void setCtx(Context ctx) {
        this.ctx = ctx;
    }

    public UserAuth getUser() {
        return user;
    }

    public void setUser(UserAuth user) {
        this.user = user;
    }

    private void validateEmail() {
        String email = emailEditText.getEditText().getText().toString().trim();
        // return email.contains("@");
        if (isTextEmpty(email)) {
            emailEditText.setError(ctx.getString(R.string.emptyField));
        } else if (!email.contains("@")) {
            emailEditText.setError(ctx.getString(R.string.notEmail));
        }else {
            emailEditText.setError(null);
        }
    }

    private void validatePassword(){
        String password = passwordEditText.getEditText().getText().toString().trim();
        if (isTextEmpty(password)){
            passwordEditText.setError(ctx.getString(R.string.emptyField));
        }else{
            passwordEditText.setError(null);
        }
    }

    private boolean isTextEmpty(String text) {
        return text.isEmpty();
    }
    private void existEmail(){
        boolean ans;
        ApolloConnector.setupApollo().query(
                GetUserByEmailQuery
                .builder()
                .email(user.getEmail()).build()
        ).enqueue(new ApolloCall.Callback<GetUserByEmailQuery.Data>() {
            @Override
            public void onResponse(@NotNull Response<GetUserByEmailQuery.Data> response) {

            }

            @Override
            public void onFailure(@NotNull ApolloException e) {

            }
        });
    }
    public void users(){
        ApolloConnector.setupApollo().query(
                GetUsersQuery
                .builder()
                .build()
        ).enqueue(new ApolloCall.Callback<GetUsersQuery.Data>() {
            @Override
            public void onResponse(@NotNull Response<GetUsersQuery.Data> response) {

                Log.d(TAG, "ans:"+response.getData().users().get(1).id());
                System.out.println("======================================:"+response.getData().users().get(1).id());
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                Toast.makeText(ctx,e.toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void singIn() {
        /*
        Intent intent = new Intent(ctx, RegisterActivity.class);
        ctx.startActivity(intent);*/
        users();
    }


}
