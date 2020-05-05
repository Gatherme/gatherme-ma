package com.example.gatherme.Register.View.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.ExistUsernameQuery;
import com.example.GetUserByEmailQuery;
import com.example.gatherme.Authentication.View.Activities.LoginActivity;
import com.example.gatherme.Enums.FieldStatus;
import com.example.gatherme.R;
import com.example.gatherme.Register.Repository.UserRegisterModel;
import com.example.gatherme.Register.ViewModel.RegisterViewModel;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "RegisterActivity";
    //EditText
    private TextInputLayout nameEditText;
    private TextInputLayout usernameEditText;
    private TextInputLayout emailEditText;
    private TextInputLayout passwordEditText;
    private TextInputLayout cityEditText;
    private TextInputLayout ageEditText;
    //Radio group
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    //Button
    private Button button;
    //ViewModel
    private RegisterViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        configView();

    }

    private void configView() {
        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        //editText
        nameEditText = findViewById(R.id.nameEditText);
        usernameEditText = findViewById(R.id.usernameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        cityEditText = findViewById(R.id.cityEditText);
        ageEditText = findViewById(R.id.ageEditText);
        //button
        button = findViewById(R.id.buttonSingUp2);
        //Radio group
        radioGroup = findViewById(R.id.radioGroupSingUP);
        checkButton();
        //onClick
        button.setOnClickListener((View.OnClickListener) this);
    }

    @Override
    public void onClick(View v) {
        //Get values
        String name = nameEditText.getEditText().getText().toString();
        String username = usernameEditText.getEditText().getText().toString();
        String email = emailEditText.getEditText().getText().toString();
        String password = passwordEditText.getEditText().getText().toString();
        String city = cityEditText.getEditText().getText().toString();
        String age = ageEditText.getEditText().getText().toString();
        //validate data
        FieldStatus emailStatus = viewModel.validateEmailField(email);
        FieldStatus passwordStatus = viewModel.validatePasswordField(password);
        boolean[] validation = new boolean[6];
        validation[0] = validateEmail(emailStatus);
        validation[1] = validatePassword(passwordStatus);

        FieldStatus nameStatus = viewModel.validateField(name);
        FieldStatus usernameStatus = viewModel.validateUsername(username);
        FieldStatus cityStatus = viewModel.validateField(city);
        FieldStatus ageStatus = viewModel.validateField(age);

        validation[2] = validateField(nameStatus, nameEditText);
        validation[3] = validateUsername(usernameStatus);
        validation[4] = validateField(cityStatus, cityEditText);
        validation[5] = validateField(ageStatus, ageEditText);
        //Register
        if (validation[0] && validation[1] && validation[2] && validation[3] && validation[4] && validation[5]) {
            int ageI = Integer.parseInt(age);
            String gender = radioButton.getText().toString();
            UserRegisterModel user = new UserRegisterModel(username, name, password, email, gender, ageI, city);
            user.setBasicActivities();
            user.setBasicCommunities();
            user.setBasicGathers();
            user.setBasicLikes();
            viewModel.setUser(user);
            viewModel.setCtx(this);
            //exist email or user
            viewModel.existEmail(new ApolloCall.Callback<GetUserByEmailQuery.Data>() {
                @Override
                public void onResponse(@Nullable Response<GetUserByEmailQuery.Data> response) {
                    if (response.getData() != null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                validateEmail(FieldStatus.EMAIL_IS_TAKEN);
                            }
                        });

                    } else {
                        viewModel.existUsername(new ApolloCall.Callback<ExistUsernameQuery.Data>() {
                            @Override
                            public void onResponse(@Nullable Response<ExistUsernameQuery.Data> response) {
                                if (response.getData() != null) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            validateUsername(FieldStatus.USERNAME_IS_TAKEN);

                                        }
                                    });

                                } else {
                                    //Register and save user info
                                    viewModel.setCtx(getApplicationContext());
                                    viewModel.registerUser();
                                    viewModel.toHome();
                                    finish();
                                }
                            }

                            @Override
                            public void onFailure(@NotNull ApolloException e) {
                                Log.e(TAG, "Username Error: " + e.getMessage());
                            }
                        });
                    }

                }

                @Override
                public void onFailure(@NotNull ApolloException e) {
                    Log.e(TAG, "Message Error: " + e.getMessage());
                }
            });


        }

    }

    public void checkButton() {
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
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
            case EMAIL_IS_TAKEN:
                emailEditText.setError(getString(R.string.emailIsTaken));
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

    private boolean validateUsername(FieldStatus statusCode) {
        boolean ans = false;
        switch (statusCode) {
            case EMPTY_FIELD:
                usernameEditText.setError(getString(R.string.emptyField));
                break;
            case USERNAME_IS_TAKEN:
                usernameEditText.setError(getString(R.string.usernameIsTaken));
                break;
            case OK:
                ans = true;
                usernameEditText.setError(null);
                break;
        }
        return ans;
    }

    private boolean validateField(FieldStatus statusCode, TextInputLayout myEditText) {
        boolean ans = false;
        switch (statusCode) {
            case EMPTY_FIELD:
                myEditText.setError(getString(R.string.emptyField));
                break;
            case OK:
                myEditText.setError(null);
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
                        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        thread.start();
    }
}


