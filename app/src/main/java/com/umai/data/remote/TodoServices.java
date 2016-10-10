package com.umai.data.remote;

import com.umai.data.model.Todo;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by Rajan Maurya on 10/10/16.
 */

public interface TodoServices {

    String ENDPOINT = "https://dl.dropboxusercontent.com/";

    @GET("u/6890301/tasks.json")
    Observable<Todo> getTodos();

    /******** Helper class that sets up a new services *******/
    class Creator {

        public static TodoServices newTodosService() {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(TodoServices.ENDPOINT)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            return retrofit.create(TodoServices.class);
        }
    }
}
