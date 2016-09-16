package com.andres.wherearemyuser.webservices;

import com.andres.wherearemyuser.dataobjects.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by andresdavid on 16/09/16.
 */
public interface GetUser {

    @GET("/users")
    Call<List<User>> getUsers();
}
