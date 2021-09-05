package com.zerdikarabulut.bugunkuyemegim.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.zerdikarabulut.bugunkuyemegim.R;
import com.zerdikarabulut.bugunkuyemegim.model.yemekModel;
import com.zerdikarabulut.bugunkuyemegim.view.DetayActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HomeAdapter extends PagerAdapter {

    //model türünde liste tanımlanıyor
    private List<yemekModel> models;

    //xml tasarımını inflate etmek için layoutInflater tanımlanıyor
    private LayoutInflater layoutInflater;

    //hangi contex de tutulacağı bilgisi için context tanımlanıyor
    private Context context;

    public HomeAdapter(List<yemekModel> models, Context context) {
        this.models = models;
        this.context = context;
    }

    //Satır sayısı
    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    //layout da gösterilecek bilgilerin ataması yapılıyor
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item, container, false);

        ImageView yemekResim;
        TextView yemekAciklama, yemekAd;

        yemekResim = view.findViewById(R.id.image);
        yemekAd = view.findViewById(R.id.title);
        yemekAciklama = view.findViewById(R.id.desc);

        Picasso.get().load(models.get(position).getYemek_resim()).into(yemekResim);
        yemekAd.setText(models.get(position).getYemek_adi());
        yemekAciklama.setText(models.get(position).getYemek_aciklama().substring(0, 100) + "...");

        //Resime basıldığında detay sayfasına ilgili ürünün bilgileri gönderiliyor
        yemekResim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetayActivity.class);

                intent.putExtra("yemekAd", models.get(position).getYemek_adi());
                intent.putExtra("yemekAciklama", models.get(position).getYemek_aciklama());
                intent.putExtra("yemekTur", models.get(position).getYemek_tur());
                intent.putExtra("yemekSure", models.get(position).getYemek_pisirme_suresi());
                intent.putExtra("kisiSayisi", models.get(position).getYemek_kisi_sayisi());
                intent.putExtra("video", models.get(position).getYemek_video());
                intent.putExtra("resim", models.get(position).getYemek_resim());
                intent.putExtra("malzeme", models.get(position).getYemek_malzeme());
                intent.putExtra("id", models.get(position).getYemek_id());

                context.startActivity(intent);
            }
        });

        container.addView(view, 0);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
