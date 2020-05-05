package com.example.gatherme.EditProfile.View.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.gatherme.R;
import com.google.android.material.textfield.TextInputLayout;

public class EditDescriptionActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "EditDescriptionActivity";
    private TextInputLayout descriptionEditText;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_description);
    }

    private void configView(){
        //editText
        descriptionEditText = findViewById(R.id.descriptionTextField);
        //button
        saveButton = findViewById(R.id.saveButtonDescription);
        //onClick
        saveButton.setOnClickListener((View.OnClickListener) this);
    }

    @Override
    public void onClick(View v) {

    }
}
