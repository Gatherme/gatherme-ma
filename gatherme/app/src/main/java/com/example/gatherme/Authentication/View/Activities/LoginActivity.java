package com.example.gatherme.Authentication.View.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.UserSingInMutation;
import com.example.gatherme.Authentication.Repository.UserAuth;
import com.example.gatherme.Authentication.ViewModel.LoginViewModel;
import com.example.gatherme.Enums.FieldStatus;
import com.example.gatherme.MainActivity;
import com.example.gatherme.R;
import com.example.gatherme.Register.View.Activities.RegisterActivity;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "LoginActivity";
    private TextInputLayout emailEditText;
    private TextInputLayout passwordEditText;
    private Button singInButton;
    private Button singUpButton;
    private LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        configView();
    }

    private void configView() {
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        //editText
        emailEditText = findViewById(R.id.emailText);
        passwordEditText = findViewById(R.id.PasswordText);
        //Buttons
        singInButton = findViewById(R.id.buttonSingin);
        singUpButton = findViewById(R.id.buttonSingUp1);
        //onClick
        singInButton.setOnClickListener((View.OnClickListener) this);
        singUpButton.setOnClickListener((View.OnClickListener) this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonSingin:
                String email = emailEditText.getEditText().getText().toString();
                String password = passwordEditText.getEditText().getText().toString();
                FieldStatus emailStatus = viewModel.validateEmailField(email);
                FieldStatus passwordStatus = viewModel.validatePasswordField(password);
                if(validateEmail(emailStatus)&&validatePassword(passwordStatus)){
                    viewModel.setUser(new UserAuth(email,password));
                    viewModel.setCtx(this);
                    viewModel.singIn(new ApolloCall.Callback<UserSingInMutation.Data>() {
                        @Override
                        public void onResponse(@NotNull Response<UserSingInMutation.Data> response) {
                            if(response.getData() == null){
                                String message = getString(R.string.emailOrPasswordError);
                                Log.e(TAG,message);
                                showToast(message);
                            }else{
                                viewModel.toHome();
                                showToast(getString(R.string.welcome));
                            }
                        }

                        @Override
                        public void onFailure(@NotNull ApolloException e) {

                        }
                    });
                }else {
                    Toast.makeText(this,"Error",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.buttonSingUp1:
                openSingUp();
                break;
        }
    }

    private void openSingUp() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private boolean validateEmail(FieldStatus statusCode) {
        boolean ans = false;
        switch (statusCode) {
            case EMPTY_FIELD:
                emailEditText.setError(getString(R.string.emptyField));
                break;
            case EMAIL_FORMAT_ERROR:
                emailEditText.setError(getString(R.string.notEmail));
                break;
            case EMAIL_NOT_FOUND:
                emailEditText.setError(getString(R.string.emailNotFound));
                break;
            case OK:
                emailEditText.setError(null);
                ans = true;
                break;
        }
        return  ans;
    }

    private boolean validatePassword(FieldStatus statusCode) {
        boolean ans = false;
        switch (statusCode) {
            case EMPTY_FIELD:
                passwordEditText.setError(getString(R.string.emptyField));
                break;
            case PASSWORD_ERROR:
                passwordEditText.setError(getString(R.string.passwordError));
                break;
            case OK:
                emailEditText.setError(null);
                ans = true;
                break;
        }
        return ans;
    }
    private void showToast(String message){
        Thread thread = new Thread(){
            public void run(){
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        thread.start();
    }
}
