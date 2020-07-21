package com.jiayou.githubsuser.services;

import com.jiayou.githubsuser.model.DetailResponse;
import com.jiayou.githubsuser.model.Item;
import com.jiayou.githubsuser.model.ItemResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    //search
    @GET("/search/users")
    Call<Item> getUser(@Query("q")String userValue);

    //detail
    @GET("/users/{username}")
    Call<DetailResponse> getDetail(@Path("username") String username);

    //followers
    @GET("/users/{username}/followers")
    Call<List<ItemResponse>> getFollower(@Path("username") String username);

    //following
    @GET("/users/{username}/following")
    Call<List<ItemResponse>> getFollowing(@Path("username") String username);

}
