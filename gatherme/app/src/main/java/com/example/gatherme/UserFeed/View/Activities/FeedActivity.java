package com.example.gatherme.UserFeed.View.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.gatherme.R;
import com.example.gatherme.UserFeed.ViewModel.UserFeedViewModel;

public class FeedActivity extends AppCompatActivity implements View.OnClickListener {
    private Button singOut;
    private UserFeedViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        configView();
    }

    private void configView() {
        viewModel = new ViewModelProvider(this).get(UserFeedViewModel.class);
        //Button
        singOut = findViewById(R.id.buttonLogout);
        singOut.setOnClickListener((View.OnClickListener) this);
    }

    @Override
    public void onClick(View v) {
        viewModel.setCtx(getApplicationContext());
        viewModel.singOut();
        finish();
    }
}
