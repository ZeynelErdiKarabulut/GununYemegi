package com.zerdikarabulut.bugunkuyemegim.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zerdikarabulut.bugunkuyemegim.R;
import com.zerdikarabulut.bugunkuyemegim.adapter.RecyclerViewAdapter;
import com.zerdikarabulut.bugunkuyemegim.model.yemekModel;
import com.zerdikarabulut.bugunkuyemegim.service.YemekAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    CompositeDisposable compositeDisposable;
    TextView txt_sonuc;
    LinearLayout linearLayout;
    ArrayList<yemekModel> yemekModels;
    private String baseURL = "https://ibrahimekinci.com/";
    Retrofit retrofit;
    String kelime;
    private final int REQ_CODE_SPEECH_INPUT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.rcy_Search);
        txt_sonuc = findViewById(R.id.txt_sonuc);
        linearLayout = findViewById(R.id.lyt_txt);

        Intent i = getIntent();
        kelime = (String) i.getSerializableExtra("kelime");
        getir();
    }

    public void getir() {
        //Retrofit && JSON
        Gson gson = new GsonBuilder().setLenient().create();
        retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        loadData();
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
        yemekModels = new ArrayList<>(yemekList);

        int urunSayisi = 0;

        //Aratılan kelime nin sağladığı şartlar bulunuyor.
        for (yemekModel s : yemekList) {
            if (s.yemek_adi.toLowerCase().indexOf(kelime.toLowerCase()) != -1 || kelime.toLowerCase().contains(s.yemek_tur.toLowerCase())) {
                urunSayisi++;
            }
        }
        if (urunSayisi == 0) {
            txt_sonuc.setText(kelime + " araması için sonuç bulunamadı.");
            txt_sonuc.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));


        } else {
            txt_sonuc.setText(kelime.toUpperCase() + " arama sonuçları");
            txt_sonuc.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));
        }
        String[][] yemekDizi = new String[urunSayisi--][9];

        //Kullanıcının arama işleminde önceliği yemek adına verdik,daha sonra kategoriye göre arama işlemi gerçekleştiriliyor
        int sira = 0;
        for (yemekModel s : yemekList) {
            if (s.yemek_adi.toLowerCase().indexOf(kelime.toLowerCase()) != -1) {
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

        //kategori için yaoılan arama kontrolü yemek adından sonra ilgili yemekler sıraya ekleniyor.
        boolean ayniYemek = false;
        for (yemekModel k : yemekList) {
            if (kelime.contains(k.yemek_tur.toLowerCase())) {
                for (int i = 0; i < sira; i++) {
                    if (yemekDizi[i][0] == k.getYemek_id()) {
                        ayniYemek = true;
                    }
                }
                if (!ayniYemek) {
                    yemekDizi[sira][0] = k.getYemek_id();
                    yemekDizi[sira][1] = k.getYemek_adi();
                    yemekDizi[sira][2] = k.getYemek_aciklama();
                    yemekDizi[sira][3] = k.getYemek_tur();
                    yemekDizi[sira][4] = k.getYemek_pisirme_suresi();
                    yemekDizi[sira][5] = k.getYemek_kisi_sayisi();
                    yemekDizi[sira][6] = k.getYemek_video();
                    yemekDizi[sira][7] = k.getYemek_resim();
                    yemekDizi[sira][8] = k.getYemek_malzeme();
                    sira++;
                }
            }
            ayniYemek = false;
        }

        //RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
        recyclerViewAdapter = new RecyclerViewAdapter(yemekModels, yemekDizi);
        recyclerView.setAdapter(recyclerViewAdapter);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();

        //Arama simgesinden sonra editText e yapılan girişi kontrol ediyoruz
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                kelime = query;
                getir();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        //Mikrofona basılmışsa google asistanı çağır
        if (id == R.id.mic) {
            promptSpeechInput();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Google asistan
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    //Google asistan da gelen cevap
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    kelime = result.get(0);
                    getir();
                }
                break;
            }
        }
    }
}
