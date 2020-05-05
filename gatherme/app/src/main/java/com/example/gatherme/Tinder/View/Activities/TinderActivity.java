package com.example.gatherme.Tinder.View.Activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.CreateSuggestMutation;
import com.example.GetSuggestionQuery;
import com.example.UserByUsernameQuery;
import com.example.gatherme.R;
import com.example.gatherme.Tinder.ViewModel.TinderViewModel;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TinderActivity extends AppCompatActivity {
    private static final String TAG = "TinderActivity";
    private ArrayList<String> al;
    private ArrayAdapter<String> arrayAdapter;
    private int i;
    private TinderViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        al = new ArrayList<>();
        setContentView(R.layout.activity_tinder);
        viewModel = new ViewModelProvider(this).get(TinderViewModel.class);
        viewModel.setId_user("1");
        al.add("Tomás");

        viewModel.createSuggestion(new ApolloCall.Callback<CreateSuggestMutation.Data>() {
            @Override
            public void onResponse(@NotNull Response<CreateSuggestMutation.Data> response) {
                String message = "********************** Creando mutacion ******************************";
                Log.d(TAG,message);

                viewModel.getSuggestion(new ApolloCall.Callback<GetSuggestionQuery.Data>(){

                    @Override
                    public void onResponse(@NotNull Response<GetSuggestionQuery.Data> response) {
                        if(response.getData() == null){
                            String message = "********************** sin sugerencias ******************************";
                            Log.d(TAG,message);

                        }else{

                            String message = "********************** Sugerencias ******************************";
                            List<GetSuggestionQuery.GetSuggestion> xx = response.getData().getSuggestion();
                            for ( GetSuggestionQuery.GetSuggestion y: xx) {
                                String user = y.suggestedUser().name();
                                Log.d(TAG, user + "kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk");
                                viewModel.setUsername(user);
                                viewModel.userByUsername(new ApolloCall.Callback<UserByUsernameQuery.Data>() {
                                    @Override
                                    public void onResponse(@NotNull Response<UserByUsernameQuery.Data> response) {
                                        if(response.getData() == null){
                                            String message = "********************** sin usuario ******************************";
                                            Log.d(TAG,message);

                                        }else{
                                            String message = "********************** un suario ******************************";
                                            String nombre = response.getData().userByUsername().name();
                                            al.add(nombre);
                                            Log.d(TAG, nombre);
                                        }
                                    }

                                    @Override
                                    public void onFailure(@NotNull ApolloException e) {
                                        String message = "********************** Error trayendo el usuario ******************************";
                                        Log.e(TAG,message);
                                    }
                                });


                            }
                            Log.d(TAG, message);
                        }
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {
                        String message = "********************** Error creando mutacion ******************************";
                        Log.e(TAG,message);
                    }
                });


            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                String message = "aja";
                Log.e(TAG,message);
            }
        });






        arrayAdapter = new ArrayAdapter<>(this, R.layout.item, R.id.helloText, al );
        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);
        flingContainer.setAdapter(arrayAdapter);




        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                al.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
                // viewModel.singIn(new ApolloCall.Callback<UserSingInMutation.Data>() {


                Toast.makeText(TinderActivity.this, "left", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                Toast.makeText(TinderActivity.this, "right", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here
                /*
                al.add("Sin nombre ".concat(String.valueOf(i)));
                arrayAdapter.notifyDataSetChanged();
                Log.d("LIST", "notified");
                i++;
                */

            }

            @Override
            public void onScroll(float scrollProgressPercent) {

            }
        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Toast.makeText(TinderActivity.this, "click", Toast.LENGTH_SHORT).show();
            }
        });

    }


}
