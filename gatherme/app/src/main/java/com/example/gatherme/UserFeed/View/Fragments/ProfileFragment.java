package com.example.gatherme.UserFeed.View.Fragments;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.GetUserByIdQuery;
import com.example.gatherme.EditProfile.View.Activities.EditDescriptionActivity;
import com.example.gatherme.EditProfile.View.Activities.EditProfileImageActivity;
import com.example.gatherme.R;
import com.example.gatherme.UserFeed.ViewModel.ProfileViewModel;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.jetbrains.annotations.NotNull;

public class ProfileFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "ProfileFragment";
    private TextView name;
    private TextView userName;
    private TextView description;
    private ImageView photo;
    private ProfileViewModel viewModel;
    private ImageButton editPhoto;
    private ImageButton editDesc;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        name = view.findViewById(R.id.name_textView);
        userName = view.findViewById(R.id.usernameTextView);
        description = view.findViewById(R.id.description_textView);
        photo = view.findViewById(R.id.profileImg);
        editPhoto = view.findViewById(R.id.imageButtonEditImage);
        editDesc = view.findViewById(R.id.imageButtonEditDescription);
        editDesc.setOnClickListener(this);
        editPhoto.setOnClickListener(this);
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        viewModel.setCtx(getContext());
        viewModel.userInfo(new ApolloCall.Callback<GetUserByIdQuery.Data>() {
            @Override
            public void onResponse(@NotNull Response<GetUserByIdQuery.Data> response) {


                Handler uiHandler = new Handler(Looper.getMainLooper());
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        //Round image with a library
                        Transformation transformation = new RoundedTransformationBuilder()
                                .cornerRadius(500)
                                .oval(false)
                                .build();
                        //Set image
                        Picasso.get()
                                .load(viewModel.getUser().getPicture())
                                .placeholder(R.drawable.blankemploy)
                                .resize(200, 200)
                                .transform(transformation)
                                .into(photo);
                        name.setText(viewModel.getUser().getName());
                        userName.setText(viewModel.getUser().getUsername());
                        description.setText(viewModel.getUser().getDescription());
                        description.setMovementMethod(new ScrollingMovementMethod());

                    }
                });
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {

            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.imageButtonEditImage:
                intent = new Intent(getContext(), EditProfileImageActivity.class);
                startActivity(intent);
                break;
            case R.id.imageButtonEditDescription:
                intent = new Intent(getContext(), EditDescriptionActivity.class);
                startActivity(intent);
                break;
        }

    }
}
