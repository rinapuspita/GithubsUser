package com.jiayou.githubsuser.services;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthenticationInterceptor implements Interceptor {
    private String Token;

    public AuthenticationInterceptor(String Token) {
        this.Token = Token;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Request.Builder builder = original.newBuilder()
                .header("Authorization", Token)
                .method(original.method(), original.body());
        Request request = builder.build();
        return chain.proceed(request);
    }
}

