package co.bitgray.bitgraytest.services;

import java.util.List;

import co.bitgray.bitgraytest.models.User;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by andrescamacho on 17/11/15.
 */
public interface RestUser {

    @GET("/users/{id}")
    void getUser(@Path("id") int id, Callback<User> cb);
}
