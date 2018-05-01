package com.sunday.common.converter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by wang on 2016/3/21.
 */
public class NetWorkCallback<T> implements Callback<T> {



    @Override
    public void onResponse(Call<T> call, Response<T> response) {
       // response.code()==
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {

    }
}
