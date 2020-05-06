package com.example.gatherme.UserFeed.View.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.gatherme.R;
import com.example.gatherme.UserFeed.View.Fragments.HomeFragment;
import com.example.gatherme.UserFeed.View.Fragments.ProfileFragment;
import com.example.gatherme.UserFeed.ViewModel.UserFeedViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FeedActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private Button singOut;
    private UserFeedViewModel viewModel;
    private BottomNavigationView bottomNavigationView;
    private static final String TAG = "FeedActivity";

    private HomeFragment  homeFragment= new HomeFragment();
    private ProfileFragment profileFragment = new ProfileFragment();
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
        singOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.setCtx(getApplicationContext());
                viewModel.singOut();
                finish();

            }
        });
        //Nav view
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        //Selected
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.i(TAG,"onNavigationSelected: ");
        boolean ans = false;
        switch (item.getItemId()){
            case R.id.navigation_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragment);
                ans = true;
                break;
            case R.id.navigation_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.container,profileFragment);
                ans = true;
                break;
            case R.id.navigation_tinder:
                Log.i(TAG,"Nothing");
                ans = true;
                break;

        }
        return ans;
    }
}
