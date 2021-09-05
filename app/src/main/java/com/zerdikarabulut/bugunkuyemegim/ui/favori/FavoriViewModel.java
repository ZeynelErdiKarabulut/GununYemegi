package com.zerdikarabulut.bugunkuyemegim.ui.favori;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

public class FavoriViewModel extends ViewModel {


    private MutableLiveData<String> mText;

    public FavoriViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Favori fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
