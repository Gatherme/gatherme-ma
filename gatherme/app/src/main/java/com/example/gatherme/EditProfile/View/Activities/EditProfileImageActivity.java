package com.example.gatherme.EditProfile.View.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.GetUserByIdQuery;
import com.example.UpdateUserMutation;
import com.example.gatherme.EditProfile.ViewModel.EditDescriptionViewModel;
import com.example.gatherme.EditProfile.ViewModel.EditProfileImageViewModel;
import com.example.gatherme.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.jetbrains.annotations.NotNull;


public class EditProfileImageActivity extends AppCompatActivity {
    private Button buttonSave;
    private ImageView photo;
    private StorageReference mStorage;
    private Uri uri;
    private static final String TAG = "EditProfileImgActivity";
    static final int SELECT_IMAGE = 1000;
    private EditDescriptionViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_image);
        configView();
    }


    private void configView() {
        buttonSave = findViewById(R.id.save_image);
        photo = findViewById(R.id.EditProfileImg);
        mStorage = FirebaseStorage.getInstance().getReference();
        viewModel = new ViewModelProvider(this).get(EditProfileImageViewModel.class);
        viewModel.setCtx(this);
        getData();
        handlePermission();
    }

    public void selectImage(View view) {
        //click on image
        openImageChooser();
    }

    public void onClick(View view) {
        //Click save button
        if (uri != null) {
            String aux = uri.getLastPathSegment();
            StorageReference filepath = mStorage.child("Fotos").child(viewModel.getUser().getUsername()+aux);
            UploadTask uploadTask = filepath.putFile(uri);
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return filepath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        String downloadURL = downloadUri.toString();
                        viewModel.getUser().setPicture(downloadURL);
                        updateUserData();
                        Log.d(TAG, "onComplete: "+downloadURL);
                    } else {
                        showToast("Error");
                    }
                }
            });
        } else {
            //TODO error
            showToast("Error");
        }
    }

    private void handlePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PermissionChecker.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    SELECT_IMAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case SELECT_IMAGE:
                for (int i = 0; i < permissions.length; i++) {
                    String permission = permissions[i];
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(
                                this, permission);
                        if (showRationale) {
                            Log.d(TAG, "onRequestPermissionsResult: showRational = true");
                        } else {
                            //user tapped never ask again
                            showSettingsAlert();
                        }
                    } else {
                        Log.i(TAG, "onRequestPermissionsResult: permissions ok");
                    }
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void showSettingsAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        //TODO idiom
        alertDialog.setTitle(getString(R.string.alert));
        alertDialog.setMessage(getString(R.string.permissions_storage));
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getText(R.string.dont_allow), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getText(R.string.settings), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                openAppSettings();
            }
        });
        alertDialog.show();
    }

    private void openAppSettings() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setData(Uri.parse("package:" + getPackageName()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        startActivity(intent);
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction((Intent.ACTION_GET_CONTENT));
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_image)), SELECT_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (resultCode == RESULT_OK) {
                    final Uri selectedImageUri = data.getData();
                    if (selectedImageUri != null) {
                        photo.post(new Runnable() {
                            @Override
                            public void run() {
                                buttonSave.setEnabled(true);
                                //Uri
                                uri = selectedImageUri;
                                //Round image with a library
                                Transformation transformation = new RoundedTransformationBuilder()
                                        .cornerRadiusDp(500)
                                        .oval(false)
                                        .build();
                                //Set image
                                Picasso.get()
                                        .load(uri)
                                        .placeholder(R.drawable.blankemploy)
                                        .resize(200, 200)
                                        .transform(transformation)
                                        .into(photo);


                                //photo.setImageURI(selectedImageUri);
                            }
                        });
                    }
                }
            }
        }).start();
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showToast(String message) {
        Thread thread = new Thread() {
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(EditProfileImageActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        thread.start();
    }

    private void getData() {
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
                } catch (NumberFormatException e) {
                    Log.e(TAG, e.getMessage());
                    showToast(getString(R.string.toast_error_data));
                }
                photo.post(new Runnable() {
                    @Override
                    public void run() {
                        //Round image with a library
                        Transformation transformation = new RoundedTransformationBuilder()
                                .cornerRadiusDp(500)
                                .oval(false)
                                .build();
                        //Set image
                        Picasso.get()
                                .load(viewModel.getUser().getPicture())
                                .placeholder(R.drawable.blankemploy)
                                .resize(200, 200)
                                .transform(transformation)
                                .into(photo);


                        //photo.setImageURI(selectedImageUri);
                    }
                });

            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                Log.e(TAG, e.getMessage());
            }
        });
    }

    private void updateUserData() {
        viewModel.updateUserData(new ApolloCall.Callback<UpdateUserMutation.Data>() {
            @Override
            public void onResponse(@NotNull Response<UpdateUserMutation.Data> response) {
                showToast(getString(R.string.data_update));
                viewModel.toHome();
                finish();
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                Log.e(TAG, e.getMessage());
            }
        });
    }
}
