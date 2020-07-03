package com.example.gatherme.UserFeed.View.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.GetActivitiesQuery;
import com.example.gatherme.R;
import com.example.gatherme.UserFeed.View.Controls.ActivityAdapter;
import com.example.gatherme.UserFeed.ViewModel.UserFeedViewModel;
import com.google.android.material.appbar.MaterialToolbar;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private UserFeedViewModel viewModel;
    private MaterialToolbar toolbar;
    private ListView listView;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        viewModel = new ViewModelProvider(this).get(UserFeedViewModel.class);
        toolbar = v.findViewById(R.id.topAppBar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                viewModel.setCtx(getContext());
                viewModel.singOut();
                return true;
            }
        });
        setHasOptionsMenu(true);
        listView = v.findViewById(R.id.list_activities);
        getActivities();
        return v;
    }

    public void getActivities() {
        viewModel.setCtx(getContext());
        viewModel.getActivities(new ApolloCall.Callback<GetActivitiesQuery.Data>() {
            @Override
            public void onResponse(Response<GetActivitiesQuery.Data> response) {
                Handler uiHandler = new Handler(Looper.getMainLooper());
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listView.setAdapter(new ActivityAdapter(Objects.requireNonNull(getContext()), viewModel.getActivityList()));
                    }
                });

            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                Log.e(TAG, "Error on get activities");
            }
        });
    }


    /**
     * ========================================================================================
     **/
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


}
