package com.example.gatherme.Tinder.View.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.CreateRequestMutation;
import com.example.CreateSuggestMutation;
import com.example.GetSuggestionQuery;
import com.example.UserByUsernameQuery;
import com.example.gatherme.Data.Database.SharedPreferencesCon;
import com.example.gatherme.Enums.Reference;
import com.example.gatherme.R;
import com.example.gatherme.Tinder.ViewModel.TinderViewModel;
import com.example.gatherme.UserFeed.View.Activities.FeedActivity;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TinderActivity extends AppCompatActivity {

    private static final String TAG = "TinderActivity";
    private TinderViewModel viewModel;
    private arrayAdapter arrayAdapter;
    List<Card> rowItems;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // se obtienen las sugerencias
        rowItems = sugerenciasCards();


        // si inicializa el adaptador
        arrayAdapter = new arrayAdapter(this, R.layout.item, rowItems );
        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);
        flingContainer.setAdapter(arrayAdapter);


        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", TAG+"removed object!");
                rowItems.remove(0);
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
                // se envia la solicitud de amistad
                enviarSolicitudAmistad((Card) dataObject);
                Toast.makeText(TinderActivity.this, "Solicitud enviada", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here
                //Toast.makeText(TinderActivity.this, "empty", Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(getApplicationContext(), FeedActivity.class);
            }

            @Override
            public void onScroll(float scrollProgressPercent) {

            }
        });

        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                //Toast.makeText(TinderActivity.this, "click", Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(getApplicationContext(), FeedActivity.class);
            }
        });
    }


    public void enviarSolicitudAmistad (Card card){
        String nick = card.getNick();
        String token = SharedPreferencesCon.read(getApplicationContext(), Reference.TOKEN);

        Log.d(TAG,"solicitud de amistad a " + nick);
        viewModel.setUser_destination(nick);
        viewModel.setToken(token);
        viewModel.createRequest(new ApolloCall.Callback<CreateRequestMutation.Data>() {

            @Override
            public void onResponse(@NotNull Response<CreateRequestMutation.Data> response) {
                Log.d(TAG,"se creo la solicitud de amistad: " + response.getData().toString());
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                Log.e(TAG,"error al crear la solicitud de amistad");
            }
        });
    }


    public List<Card> sugerenciasCards (){
        // se crea arroy de cards para sugerencias
        rowItems = new ArrayList<Card>();
        Card item = new Card("", "", "", "");
        rowItems.add(item);

        // se obtiene el ID de usuario para hacer las consultas de sugerencias
        String id = SharedPreferencesCon.read(getApplicationContext(), Reference.ID);
        Log.d(TAG,"userId" + id);


        // Creacion de sugerencias para el usuario
        setContentView(R.layout.activity_tinder);
        viewModel = new ViewModelProvider(this).get(TinderViewModel.class);
        viewModel.setId_user(id);

        viewModel.createSuggestion(new ApolloCall.Callback<CreateSuggestMutation.Data>() {
            @Override
            public void onResponse(@NotNull Response<CreateSuggestMutation.Data> response) {
                Log.d(TAG,"Sugerencias creadas");

                // se obteinen las sugerencias para el usuario
                viewModel.getSuggestion(new ApolloCall.Callback<GetSuggestionQuery.Data>(){

                    @Override
                    public void onResponse(@NotNull Response<GetSuggestionQuery.Data> response) {

                        if(response.getData() == null){
                            Log.e(TAG,"Sin sugerencias para el usuario " + id);

                        }else{

                            // Se iteran las sugerencias
                            List<GetSuggestionQuery.GetSuggestion> sugerencias = response.getData().getSuggestion();
                            for ( GetSuggestionQuery.GetSuggestion sugerencia: sugerencias) {
                                String user = sugerencia.suggestedUser().name();
                                Log.d(TAG, "se sugire al usuario: " + user );

                                viewModel.setUsername(user);

                                // se obtiene el los datos del usuario sugerido
                                viewModel.userByUsername(new ApolloCall.Callback<UserByUsernameQuery.Data>() {
                                    @Override
                                    public void onResponse(@NotNull Response<UserByUsernameQuery.Data> response) {
                                        if(response.getData() == null){
                                            Log.e(TAG,"no se encuentra el usuario sugerido: " + user);

                                        }else{
                                            // se crea una Card con los datos del usuario sugerido
                                            Log.d(TAG, "el usuario sugerido es: " + response.toString());
                                            String nombre = response.getData().userByUsername().name();
                                            String ubicacion = response.getData().userByUsername().city();
                                            String biografia = response.getData().userByUsername().description();
                                            String nick = response.getData().userByUsername().username();
                                            Card item = new Card(nombre, ubicacion, biografia, nick);
                                            rowItems.add(item);
                                        }
                                    }

                                    @Override
                                    public void onFailure(@NotNull ApolloException e) {
                                        Log.e(TAG,"error al obnerer los datos del usuario sugerido: " + user);
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {
                        Log.e(TAG,"error al ver las sugerencias");
                    }
                });
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                Log.e(TAG,"error al crear sugerencias");
            }
        });
        return rowItems;
    }

}
