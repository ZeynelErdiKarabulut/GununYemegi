package com.zerdikarabulut.bugunkuyemegim.service;

import com.zerdikarabulut.bugunkuyemegim.model.yemekModel;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;

public interface YemekAPI {
    //https://ibrahimekinci.com/yemekapp/yemekler.json
    @GET("yemekapp/yemekler.json")
    Observable<List<yemekModel>> getData();
}
