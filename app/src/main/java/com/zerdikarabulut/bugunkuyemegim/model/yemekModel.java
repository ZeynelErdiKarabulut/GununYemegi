package com.zerdikarabulut.bugunkuyemegim.model;

import com.google.gson.annotations.SerializedName;

public class yemekModel {

    @SerializedName("yemek_id")
    public String yemek_id;

    @SerializedName("yemek_adi")
    public String yemek_adi;

    @SerializedName("yemek_aciklama")
    public String yemek_aciklama;

    @SerializedName("yemek_tur")
    public String yemek_tur;

    @SerializedName("yemek_pisirme_suresi")
    public String yemek_pisirme_suresi;

    @SerializedName("yemek_kisi_sayisi")
    public String yemek_kisi_sayisi;

    @SerializedName("yemek_video")
    public String yemek_video;

    @SerializedName("yemek_resim")
    public String yemek_resim;

    @SerializedName("yemek_malzeme")
    public String yemek_malzeme;

    public yemekModel(String yemek_id, String yemek_adi, String yemek_aciklama, String yemek_tur, String yemek_pisirme_suresi, String yemek_kisi_sayisi, String yemek_video, String yemek_resim, String yemek_malzeme) {
        this.yemek_id = yemek_id;
        this.yemek_adi = yemek_adi;
        this.yemek_aciklama = yemek_aciklama;
        this.yemek_tur = yemek_tur;
        this.yemek_pisirme_suresi = yemek_pisirme_suresi;
        this.yemek_kisi_sayisi = yemek_kisi_sayisi;
        this.yemek_video = yemek_video;
        this.yemek_resim = yemek_resim;
        this.yemek_malzeme = yemek_malzeme;
    }

    @Override
    public String toString() {
        return "yemekModel{" +
                "yemek_id='" + yemek_id + '\'' +
                ", yemek_adi='" + yemek_adi + '\'' +
                ", yemek_aciklama='" + yemek_aciklama + '\'' +
                ", yemek_tur='" + yemek_tur + '\'' +
                ", yemek_pisirme_suresi='" + yemek_pisirme_suresi + '\'' +
                ", yemek_kisi_sayisi='" + yemek_kisi_sayisi + '\'' +
                ", yemek_video='" + yemek_video + '\'' +
                ", yemek_resim='" + yemek_resim + '\'' +
                ", yemek_malzeme='" + yemek_malzeme + '\'' +
                '}';
    }

    public String getYemek_id() {
        return yemek_id;
    }

    public String getYemek_adi() {
        return yemek_adi;
    }

    public String getYemek_aciklama() {
        return yemek_aciklama;
    }

    public String getYemek_tur() {
        return yemek_tur;
    }

    public String getYemek_pisirme_suresi() {
        return yemek_pisirme_suresi;
    }

    public String getYemek_kisi_sayisi() {
        return yemek_kisi_sayisi;
    }

    public String getYemek_video() {
        return yemek_video;
    }

    public String getYemek_resim() {
        return yemek_resim;
    }

    public String getYemek_malzeme() {
        return yemek_malzeme;
    }

}
