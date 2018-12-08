package com.example.joao.facesenac.pi.activity.interfaces;

import com.example.joao.facesenac.pi.activity.model.Curtidas;
import com.example.joao.facesenac.pi.activity.model.GetFeed;
import com.example.joao.facesenac.pi.activity.model.GetFriends;
import com.example.joao.facesenac.pi.activity.model.PostFeed;
import com.example.joao.facesenac.pi.activity.model.PostUserLogin;
import com.example.joao.facesenac.pi.activity.model.SigninBody;
import com.example.joao.facesenac.pi.activity.model.SignupBody;
import com.example.joao.facesenac.pi.activity.model.RetornoAmigos;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiUsers {

    @Headers({
        "Accept: application/json;charset=utf-8",
        "Content-Type: application/json;charset=utf-8"
    })
    @POST("login")
    Call<PostUserLogin> postLogin(@Body SigninBody user);

    @Headers({
        "Accept: application/json;charset=utf-8",
        "Content-Type: application/json;charset=utf-8"
    })
    @POST("users")
    Call<Long> postSignup(@Body SignupBody info);


    @POST("posts")
    Call<Long> postFeed(@Body PostFeed postFeeder);

    @POST("{usuario}/{historico}")
    Call<Curtidas> postComment(@Path("usuario") Long user, @Path("historico") Long history);

    @GET("friends/{id}")
    Call<ArrayList<GetFeed>> getPosts(@Path("id") Long id);

    @GET("friends/{id}")
    Call<ArrayList<GetFeed>> getPostAmigo(@Path("id") Long id);

    @GET("search/{id}")
    Call<ArrayList<GetFriends>> getFriendsAPI(@Path("id") Long id);

    @DELETE("friends/{idUsuario}/{idAmizade}")
    Call<RetornoAmigos> deletAmizade(@Path("idUsuario") Long idUsuario, @Path("idAmizade") Long idAmizade);

    @PUT("friends/{idUsuario}/{idAmizade}")
    Call<RetornoAmigos> putFriends(@Path("idUsuario") Long idUsuario, @Path("idAmizade") Long idAmizade);

    @POST("friends/{idUsuario}/{idAmizade}")
    Call<RetornoAmigos> postFriends(@Path("idUsuario") Long idUsuario, @Path("idAmizade") Long idAmizade);

    @GET("posts/{id}")
    Call<ArrayList<GetFeed>> getMyPosts(@Path("id") Long id);
}
