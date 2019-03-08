package com.example.android.instagram.httpservice;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpClientService {
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();


        retrofit = new Retrofit.Builder()
                .baseUrl("http://18a2190c.ngrok.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofit;
    }
}

//    OkHttpClient.Builder builder = new OkHttpClient.Builder();
//builder.connectTimeout(5, TimeUnit.MINUTES) // connect timeout
//        .writeTimeout(5, TimeUnit.MINUTES) // write timeout
//        .readTimeout(5, TimeUnit.MINUTES); // read timeout
//
//        okHttpClient = builder.build();