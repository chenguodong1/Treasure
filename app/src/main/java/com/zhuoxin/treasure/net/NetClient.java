package com.zhuoxin.treasure.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by MACHENIKE on 2017/8/2.
 */

public class NetClient {
    public static final String BASE_URL = "http://admin.syfeicuiedu.com";
    private static NetClient mNetClient;
    private final Retrofit mRetrofit;
    private NetRequest mNetRequest;

    private NetClient(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        mRetrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public static synchronized NetClient getInstance(){
        if (mNetClient==null){
            mNetClient = new NetClient();
        }
        return mNetClient;
    }

    public NetRequest getNetRequest(){
        if (mNetRequest==null){
            mNetRequest = mRetrofit.create(NetRequest.class);
        }
        return  mNetRequest;
    }
}
