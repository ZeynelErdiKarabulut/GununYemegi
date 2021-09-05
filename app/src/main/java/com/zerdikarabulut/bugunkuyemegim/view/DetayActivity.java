package com.zerdikarabulut.bugunkuyemegim.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zerdikarabulut.bugunkuyemegim.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DetayActivity extends AppCompatActivity {

    TextView txt_sure, txt_kisi, txt_malzeme, txt_yapilis, txt_video, txt_yemekAadi;
    ImageView fotograf, img_favori;
    String video, id, yemekAd, yemekAciklama, yemekTur, yemekSure, kisiSayisi, resim, malzeme;
    Boolean favori = false;
    public static List<String> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detay);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //PutExtra ile gelen bilgiler değişkenlere aktarılıyor
        Intent i = getIntent();
        yemekAd = (String) i.getSerializableExtra("yemekAd");
        yemekAciklama = (String) i.getSerializableExtra("yemekAciklama");
        yemekTur = (String) i.getSerializableExtra("yemekTur");
        yemekSure = (String) i.getSerializableExtra("yemekSure");
        kisiSayisi = (String) i.getSerializableExtra("kisiSayisi");
        video = (String) i.getSerializableExtra("video");
        resim = (String) i.getSerializableExtra("resim");
        malzeme = (String) i.getSerializableExtra("malzeme");
        id = (String) i.getSerializableExtra("id");

        txt_sure = findViewById(R.id.txt_sure);
        txt_kisi = findViewById(R.id.txt_kisiSayisi);
        txt_malzeme = findViewById(R.id.txt_malzeme);
        txt_yapilis = findViewById(R.id.txt_yapilis);
        txt_video = findViewById(R.id.txt_video);
        fotograf = findViewById(R.id.img_yemekResim);
        txt_yemekAadi = findViewById(R.id.txt_yemekAdi);
        img_favori = findViewById(R.id.img_favori);

        txt_yemekAadi.setText(yemekAd);
        txt_sure.setText(yemekSure);
        txt_kisi.setText(kisiSayisi);
        txt_malzeme.setText(malzeme);
        txt_yapilis.setText(yemekAciklama);
        txt_video.setText(video);
        Picasso.get().load(resim).into(fotograf);


        //sharedPreferences ile favorideki yemek id leri bulunuyor eğer yemek favorilerde ise img_favori set ediliyor
        SharedPreferences sharedPreferences = getSharedPreferences("pref", 0);
        String idsString = sharedPreferences.getString("id", "");
        String[] itemsWords = idsString.split(",");
        items = new ArrayList<>();

        //favori id ler items listesine ekleniyor
        for (int k = 0; k < itemsWords.length; k++) {
            items.add(itemsWords[k]);
        }

        //İlgili ürünün favori kontrolü yapılıyor
        for (int j = 0; j < items.size(); j++) {
            if (items.get(j).equals(id)) {
                img_favori.setImageResource(R.drawable.ic_favorite);
                favori = true;
            }
        }
        if (!favori) {
            img_favori.setImageResource(R.drawable.ic_no_favorite);
        }
    }

    //Video string basılınca ilgili yemek video linki açılıyor.
    public void video_link(View view) {
        Uri linkimiz = Uri.parse(video); //butona vermek istediğimiz linki buraya yazıyoruz.
        Intent intentimiz = new Intent(Intent.ACTION_VIEW, linkimiz);
        startActivity(intentimiz);
    }

    //img_favori basılınca favori ekleme veye silme işlemi yapılıp resim set ediliyor.
    public void fovoriEkle(View view) {
        if (!favori) {
            items.add(id);
            StringBuilder stringBuilder = new StringBuilder();
            for (String k : items) {

                stringBuilder.append(k);
                stringBuilder.append(",");
            }
            SharedPreferences sharedPreferences = getSharedPreferences("pref", 0);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("id", stringBuilder.toString());
            editor.commit();

            favori = true;
            img_favori.setImageResource(R.drawable.ic_favorite);
        } else {
            items.remove(id);
            StringBuilder stringBuilder = new StringBuilder();
            for (String k : items) {

                stringBuilder.append(k);
                stringBuilder.append(",");
            }
            SharedPreferences sharedPreferences = getSharedPreferences("pref", 0);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("id", stringBuilder.toString());
            editor.commit();

            img_favori.setImageResource(R.drawable.ic_no_favorite);
            favori = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_detay, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        //Share butonuna basılınca ilgili tanımlamalar paylaşılıyor.
        if (id == R.id.share) {
            String body = yemekAd + "\n \nMalzemeler : \n\n" + malzeme + "\n\nYapılış : \n\n" + yemekAciklama + "\n\nKaç Kişilik : " + kisiSayisi + "\n\nSüre : " + yemekSure + "\n \nVideo : \n" + video;
            shareText(String.valueOf(R.string.share_subject), body);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void shareText(String subject, String body) {
        Intent txtIntent = new Intent(android.content.Intent.ACTION_SEND);
        txtIntent.setType("text/plain");
        txtIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        txtIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);
        startActivity(Intent.createChooser(txtIntent, "Share"));
    }
}
