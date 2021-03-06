package com.zerdikarabulut.bugunkuyemegim.ui.anaYemek;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zerdikarabulut.bugunkuyemegim.R;
import com.zerdikarabulut.bugunkuyemegim.adapter.RecyclerViewAdapter;
import com.zerdikarabulut.bugunkuyemegim.model.yemekModel;
import com.zerdikarabulut.bugunkuyemegim.service.YemekAPI;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class AnaYemekFragment extends Fragment {

    RecyclerView recyclerView;

    RecyclerViewAdapter recyclerViewAdapter;

    CompositeDisposable compositeDisposable;

    ArrayList<yemekModel> yemekModels;
    private String baseURL = "https://ibrahimekinci.com/";
    Retrofit retrofit;

    private AnaYemekViewModel anaYemekViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        anaYemekViewModel = ViewModelProviders.of(this).get(AnaYemekViewModel.class);
        View root = inflater.inflate(R.layout.fragment_anayemek, container, false);

        recyclerView = root.findViewById(R.id.rcy_AnaYemek);

        anaYemekViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

                //Retrofit && JSON
                Gson gson = new GsonBuilder().setLenient().create();
                retrofit = new Retrofit.Builder()
                        .baseUrl(baseURL)
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();

                //JSON verilerini ??ekece??imiz metodu ??a????r??yoruz
                loadData();
            }
        });
        return root;
    }

    //RxJava ile json bilgilerimizi y??kl??yoruz t??m bilgiyi hepsini de??il par??a par??a ??ekiliyor
    private void loadData() {
        YemekAPI yemekAPI = retrofit.create(YemekAPI.class);
        compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(yemekAPI.getData()
                //Observable sonucu yay??nlanacak olacak i??lemin hangi threadde ??al????mas?? gerekti??ini belirtiyoruz
                .subscribeOn(Schedulers.io())
                //Subsriber hangi thread???de dinlemesi gerekti??ini belirtiyoruz
                .observeOn(AndroidSchedulers.mainThread())
                //subscribe, Observable???a bir abone, abone oldu??unda ger??ekle??tirilecek eylemi tan??mlayan bir arabirimdir.
                //Abone olma y??ntemi yaln??zca bir Observer Observable???e abone oldu??unda ??al??????r.
                .subscribe(this::handleResponse));
    }

    private void handleResponse(List<yemekModel> yemekList) {

        yemekModels = new ArrayList<>(yemekList);//cryptoModels ArrayList'imize responList deki de??erleri kaydediyoruz.

        //kategori kontrol?? yap??yoruz  ve ilgili bilgileri yemekDizi de tutuyoruz
        int urunSayisi = 0;
        String kategori = "anaYemek";

        //Yemek say??s?? belirlenip dizi boyutu ayarlan??yor
        for (yemekModel s : yemekList) {
            if (kategori.equals(s.yemek_tur)) {
                urunSayisi++;
            }
        }

        String[][] yemekDizi = new String[urunSayisi--][9];

        int sira = 0;
        for (yemekModel s : yemekList) {
            if (kategori.equals(s.yemek_tur)) {
                yemekDizi[sira][0] = s.getYemek_id();
                yemekDizi[sira][1] = s.getYemek_adi();
                yemekDizi[sira][2] = s.getYemek_aciklama();
                yemekDizi[sira][3] = s.getYemek_tur();
                yemekDizi[sira][4] = s.getYemek_pisirme_suresi();
                yemekDizi[sira][5] = s.getYemek_kisi_sayisi();
                yemekDizi[sira][6] = s.getYemek_video();
                yemekDizi[sira][7] = s.getYemek_resim();
                yemekDizi[sira][8] = s.getYemek_malzeme();
                sira++;
            }
        }

        //RecyclerView, Dizi ve ArrayList g??nderiliyor
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewAdapter = new RecyclerViewAdapter(yemekModels, yemekDizi);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}