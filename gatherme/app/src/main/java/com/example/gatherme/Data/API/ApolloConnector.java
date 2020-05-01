package com.example.gatherme.Data.API;

import com.apollographql.apollo.ApolloClient;
import okhttp3.OkHttpClient;
public class ApolloConnector {
    private static final String DB_URL = "http://172.17.0.1:9001/graphql";
    public static ApolloClient setupApollo(){
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        return ApolloClient.builder().serverUrl(DB_URL).okHttpClient(okHttpClient).build();
    }
}
