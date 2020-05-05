package com.example.gatherme.EditProfile.View.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.GetUserByIdQuery;
import com.example.UpdateUserMutation;
import com.example.gatherme.Authentication.View.Activities.LoginActivity;
import com.example.gatherme.EditProfile.ViewModel.EditDescriptionViewModel;
import com.example.gatherme.Enums.FieldStatus;
import com.example.gatherme.R;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

public class EditDescriptionActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "EditDescriptionActivity";
    private TextInputLayout descriptionEditText;
    private Button saveButton;
    private EditDescriptionViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_description);
        configView();
        getData();
    }

    private void configView(){
        viewModel = new ViewModelProvider(this).get(EditDescriptionViewModel.class);
        //editText
        descriptionEditText = findViewById(R.id.descriptionTextField);
        //button
        saveButton = findViewById(R.id.saveButtonDescription);
        //onClick
        saveButton.setOnClickListener((View.OnClickListener) this);
    }

    @Override
    public void onClick(View v) {
        String description = descriptionEditText.getEditText().getText().toString();
        viewModel.getUser().setDescription(description);
        viewModel.setCtx(getApplicationContext());
        updateUserData();
    }

    private void getData(){
        viewModel.setCtx(getApplicationContext());
        viewModel.getUserInfo(new ApolloCall.Callback<GetUserByIdQuery.Data>() {
            @Override
            public void onResponse(@NotNull Response<GetUserByIdQuery.Data> response) {
                try {
                    GetUserByIdQuery.UserById aux = response.getData().userById();
                    viewModel.getUser().setUsername(aux.username());
                    viewModel.getUser().setName(aux.name());
                    viewModel.getUser().setEmail(aux.email());
                    viewModel.getUser().setPicture(aux.picture());
                    String description = aux.description();
                    viewModel.getUser().setDescription(description);
                    viewModel.getUser().setGender(aux.gender());
                    viewModel.getUser().setAge(aux.age());
                    viewModel.getUser().setCity(aux.city());
                    viewModel.getUser().setLikes(viewModel.getUser().transformerString(aux.likes()));
                    viewModel.getUser().setCommunities(viewModel.getUser().transformerInt(aux.communities()));
                    viewModel.getUser().setActivities(viewModel.getUser().transformerInt(aux.activities()));
                    viewModel.getUser().setGathers(viewModel.getUser().transformerString(aux.gathers()));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            descriptionEditText.getEditText().setText(description);

                        }
                    });
                }catch (NumberFormatException e){
                    Log.e(TAG,e.getMessage());
                    showToast(getString(R.string.toast_error_data));
                }
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                Log.e(TAG,e.getMessage());
            }
        });
    }
    private void showToast(String message) {
        Thread thread = new Thread() {
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(EditDescriptionActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        thread.start();
    }
    private void updateUserData(){
        viewModel.updateUserData(new ApolloCall.Callback<UpdateUserMutation.Data>() {
            @Override
            public void onResponse(@NotNull Response<UpdateUserMutation.Data> response) {
                showToast(getString(R.string.data_update));
                viewModel.toHome();
                finish();
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                Log.e(TAG,e.getMessage());
            }
        });
    }
}
