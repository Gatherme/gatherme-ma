package com.example.gatherme.Data.API;

import com.apollographql.apollo.ApolloClient;
import okhttp3.OkHttpClient;
public class ApolloConnector {
    private static final String DB_URL = "http://34.200.179.125:81/graphql";
    //private static final String DB_URL = "http://18.204.246.34:9001/graphql";
    //private static final String DB_URL = "http://34.204.84.197:9001/graphql";
    public static ApolloClient setupApollo(){
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        return ApolloClient.builder().serverUrl(DB_URL).okHttpClient(okHttpClient).build();
    }
}
