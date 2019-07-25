package com.arun.retrofit_slim.api;

import android.util.Base64;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    //private static final String BASE_URL = "http://androidarun.com/MyApi/public/";
    private static final String BASE_URL = "http://10.0.2.2/MyApi/public/";
    private static RetrofitClient mInstance;
    private Retrofit retrofit;

    public static final String AUTH = "Basic " + Base64.encodeToString(("arunrk:123456").getBytes(), Base64.NO_WRAP);

    private RetrofitClient() {

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Request.Builder builder = request.newBuilder()
                                .addHeader("Authorization", AUTH)
                                .method(request.method(), request.body());

                        Request request1  = builder.build();
                        return chain.proceed(request1);

                    }
                }).build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    public static synchronized RetrofitClient getInstance() {
         if (mInstance == null){
             mInstance = new RetrofitClient();
         }
         return mInstance;
    }

    public Api getApi() {
        return retrofit.create(Api.class);
    }

}
