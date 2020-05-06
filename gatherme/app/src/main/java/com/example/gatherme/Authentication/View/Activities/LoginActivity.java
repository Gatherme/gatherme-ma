package com.example.gatherme.Authentication.View.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.UserSingInMutation;
import com.example.gatherme.Authentication.Repository.Model.UserAuth;
import com.example.gatherme.Authentication.ViewModel.LoginViewModel;
import com.example.gatherme.Data.Database.SharedPreferencesCon;
import com.example.gatherme.Enums.FieldStatus;
import com.example.gatherme.Enums.Reference;
import com.example.gatherme.R;
import com.example.gatherme.Register.View.Activities.RegisterActivity;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "LoginActivity";
    private TextInputLayout emailEditText;
    private TextInputLayout passwordEditText;
    private Button signInButton;
    private Button signUpButton;
    private LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        configView();
        initSeason();
    }


    private void configView() {
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        //editText
        emailEditText = findViewById(R.id.emailText);
        passwordEditText = findViewById(R.id.PasswordText);
        //Buttons
        signInButton = findViewById(R.id.buttonSign_in);
        signUpButton = findViewById(R.id.buttonSignUp1);
        //onClick
        signInButton.setOnClickListener((View.OnClickListener) this);
        signUpButton.setOnClickListener((View.OnClickListener) this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonSign_in:
                String email = emailEditText.getEditText().getText().toString();
                String password = passwordEditText.getEditText().getText().toString();
                FieldStatus emailStatus = viewModel.validateEmailField(email);
                FieldStatus passwordStatus = viewModel.validatePasswordField(password);
                if (validateEmail(emailStatus) && validatePassword(passwordStatus)) {
                    viewModel.setUser(new UserAuth(email, password));
                    viewModel.setCtx(this);
                    viewModel.singIn(new ApolloCall.Callback<UserSingInMutation.Data>() {
                        @Override
                        public void onResponse(@NotNull Response<UserSingInMutation.Data> response) {
                            if (response.getData() == null) {
                                String message = getString(R.string.emailOrPasswordError);
                                Log.e(TAG, message);
                                showToast(message);
                            } else {
                                viewModel.setCtx(getApplicationContext());
                                //Used shared preferences to save user info
                                viewModel.getAndSaveUsersByEmail();

                                String token = response.getData().singIn().token();
                                SharedPreferencesCon.save(getApplicationContext(), Reference.TOKEN, token);
                                SharedPreferencesCon.save(getApplicationContext(), Reference.PASSWORD, password);

                                viewModel.toHome();
                                finish();
                                showToast(getString(R.string.welcome));
                            }
                        }

                        @Override
                        public void onFailure(@NotNull ApolloException e) {
                            Log.e(TAG, "onFailure-->"+e.getMessage());
                        }
                    });
                } else {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.buttonSignUp1:
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
        return ans;
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

    private void showToast(String message) {
        Thread thread = new Thread() {
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        thread.start();
    }

    private void initSeason() {
        String email = SharedPreferencesCon.read(getApplicationContext(), Reference.EMAIL);
        String password = SharedPreferencesCon.read(getApplicationContext(), Reference.PASSWORD);
        String defVal = SharedPreferencesCon.defValue;
        Log.d(TAG,email);
        if (!email.equals(defVal) && !password.equals(defVal)) {
            viewModel.setUser(new UserAuth(email, password));
            viewModel.setCtx(getApplicationContext());
            viewModel.singIn(new ApolloCall.Callback<UserSingInMutation.Data>() {
                @Override
                public void onResponse(@NotNull Response<UserSingInMutation.Data> response) {
                    if (response.getData() == null) {
                        String message = getString(R.string.emailOrPasswordError);
                        Log.e(TAG, message);
                        showToast(message);
                    } else {
                        viewModel.setCtx(getApplicationContext());
                        //Used shared preferences to save user info
                        viewModel.getAndSaveUsersByEmail();
                        String token = response.getData().singIn().token();
                        SharedPreferencesCon.save(getApplicationContext(), Reference.TOKEN, token);
                        SharedPreferencesCon.save(getApplicationContext(), Reference.PASSWORD, password);
                        viewModel.toHome();
                        finish();
                        showToast(getString(R.string.welcome));
                    }
                }

                @Override
                public void onFailure(@NotNull ApolloException e) {
                    Log.e(TAG, e.getMessage());
                }
            });
        }else {
            Log.i(TAG,"Not user info");
        }
    }

}
