package com.zerdikarabulut.bugunkuyemegim.ui.home;

import android.animation.ArgbEvaluator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zerdikarabulut.bugunkuyemegim.R;
import com.zerdikarabulut.bugunkuyemegim.adapter.HomeAdapter;
import com.zerdikarabulut.bugunkuyemegim.adapter.RecyclerViewAdapter;
import com.zerdikarabulut.bugunkuyemegim.model.yemekModel;
import com.zerdikarabulut.bugunkuyemegim.service.YemekAPI;
import com.zerdikarabulut.bugunkuyemegim.view.MainActivity;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    ViewPager viewPager;
    HomeAdapter adapter;
    List<yemekModel> models;
    Integer[] colors = null;
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    Button btn_yemegimiBul;
    CompositeDisposable compositeDisposable;
    private String baseURL = "https://ibrahimekinci.com/";
    Retrofit retrofit;
    String id, ad, aciklama, tur, pisirmeSuresi, kisiSayisi, video, resim, malzeme;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        viewPager = root.findViewById(R.id.viewPager);
        btn_yemegimiBul = root.findViewById(R.id.btn_yemegimiBul);

        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //Retrofit && JSON
                Gson gson = new GsonBuilder().setLenient().create();
                retrofit = new Retrofit.Builder()
                        .baseUrl(baseURL)
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();
                loadData();
            }
        });

        //Bugünkü yemeğimi? butonuna basılınca random yemek üretiliyor.
        btn_yemegimiBul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });
        return root;
    }

    private void loadData() {
        YemekAPI yemekAPI = retrofit.create(YemekAPI.class);
        compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(yemekAPI.getData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponse));
    }

    private void handleResponse(List<yemekModel> yemekList) {
        models = new ArrayList<>();

        //tüm kategoriler için random yemek bulunup models listesinde tutuluyor.
        randomFood(yemekList, "çorba");
        models.add(new yemekModel(id, ad, aciklama, tur, pisirmeSuresi, kisiSayisi, video, resim, malzeme));
        randomFood(yemekList, "anaYemek");
        models.add(new yemekModel(id, ad, aciklama, tur, pisirmeSuresi, kisiSayisi, video, resim, malzeme));
        randomFood(yemekList, "pilav");
        models.add(new yemekModel(id, ad, aciklama, tur, pisirmeSuresi, kisiSayisi, video, resim, malzeme));
        randomFood(yemekList, "salata");
        models.add(new yemekModel(id, ad, aciklama, tur, pisirmeSuresi, kisiSayisi, video, resim, malzeme));
        randomFood(yemekList, "tatlı");
        models.add(new yemekModel(id, ad, aciklama, tur, pisirmeSuresi, kisiSayisi, video, resim, malzeme));

        //adapter a ilgili model ve context tanımlaması yapılıyor
        adapter = new HomeAdapter(models, getContext());

        //----------VIEW PAGER
        viewPager.setAdapter(adapter);
        viewPager.setPadding(130, 0, 130, 0);

        //her card da farklı backgroundColor gözükmesi için renk tanımlamaları yapılıyor
        Integer[] colors_temp = {
                getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorPrimaryDark),
                getResources().getColor(R.color.color3),
                getResources().getColor(R.color.color4),
                getResources().getColor(R.color.color5),
        };

        colors = colors_temp;

        //ViewPager dinleniyor ve metodlara göre değişiklikler veya eylemler gerçekleştiriliyor
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            //itemler kaydırıldıkça arkaplan renkleri değişiyor
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position < (adapter.getCount() - 1) && position < (colors.length - 1)) {
                    viewPager.setBackgroundColor(
                            (Integer) argbEvaluator.evaluate(
                                    positionOffset,
                                    colors[position],
                                    colors[position + 1]
                            )
                    );
                } else {
                    viewPager.setBackgroundColor(colors[colors.length - 1]);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    //Kategori ye göre random yemek bulunuyor.
    public void randomFood(List<yemekModel> yemekModels1, String kategori) {
        //dizi boyutunu belirlemek için belirtilen kategoriden kaç adet yemek olduğu bulunuyor.
        int urunSayisi = 0;
        for (yemekModel s : yemekModels1) {
            if (kategori.equals(s.yemek_tur)) {
                urunSayisi++;
            }
        }
        //Gönderilen kategorideki yemkelerin id lerini dizide topluyoruz böylelikle random alıcağımız değerin aralığını belirliyoruz
        int sira = 0;
        String[] yemekDizisi = new String[urunSayisi--];
        for (yemekModel s : yemekModels1) {
            if (kategori.equals(s.yemek_tur)) {
                yemekDizisi[sira] = s.getYemek_id();
                sira++;
            }
        }

        //yemek id leri arasında random bir değer buluyoruz
        int upper, lower;
        upper = Integer.parseInt(yemekDizisi[0]);
        lower = Integer.parseInt(yemekDizisi[urunSayisi--]);
        int r = (int) (Math.random() * (upper - lower)) + lower;

        //random id li yemek bilgilerini randomFoods dizisine aktarıyoruz.
        for (yemekModel s : yemekModels1) {
            if (r == Integer.parseInt(s.yemek_id)) {
                id = s.getYemek_id();
                ad = s.getYemek_adi();
                aciklama = s.getYemek_aciklama();
                tur = s.getYemek_tur();
                pisirmeSuresi = s.getYemek_pisirme_suresi();
                kisiSayisi = s.getYemek_kisi_sayisi();
                video = s.getYemek_video();
                resim = s.getYemek_resim();
                malzeme = s.getYemek_malzeme();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}