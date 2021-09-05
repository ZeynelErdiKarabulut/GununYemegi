package com.zerdikarabulut.bugunkuyemegim.view;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zerdikarabulut.bugunkuyemegim.R;

public class SplashScreen extends AppCompatActivity {

    ImageView img_logo, img_info;
    Button positiveButton;
    TextView txt_text;
    public static int gecis_suresi = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        img_logo = findViewById(R.id.img_logo);

        //Animation
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.animation_logo);
        img_logo.startAnimation(animation);


        //Geçiş
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (internetKontrol()) {
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    showPositivePopup();
                }
            }
        }, gecis_suresi);
    }

    //interneti kontrol
    protected boolean internetKontrol() {
        // TODO Auto-generated method stub
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    //İnternet bağlantısı yoksa gösterilecek hata mesajı
    public void showPositivePopup() {
        LayoutInflater layoutInflater = getLayoutInflater();
        View alerLayout = layoutInflater.inflate(R.layout.epic_popup, null);

        img_info = alerLayout.findViewById(R.id.imageView);
        positiveButton = alerLayout.findViewById(R.id.btn_tamam);
        txt_text = alerLayout.findViewById(R.id.txt_text);

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(alerLayout);
        alert.setCancelable(false);

        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pid = android.os.Process.myPid();
                android.os.Process.killProcess(pid);
                AlertDialog dialog = alert.create();
                dialog.dismiss();
            }
        });

        AlertDialog dialog = alert.create();
        dialog.show();
    }
}
