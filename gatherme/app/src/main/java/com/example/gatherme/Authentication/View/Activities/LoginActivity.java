package com.example.gatherme.Authentication.View.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gatherme.Authentication.Repository.UserAuth;
import com.example.gatherme.Authentication.ViewModel.LoginViewModel;
import com.example.gatherme.R;
import com.example.gatherme.Register.View.Activities.RegisterActivity;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
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

    private void configView(){
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
                viewModel.users();
                Toast.makeText(this,"asf",Toast.LENGTH_SHORT).show();
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
}
