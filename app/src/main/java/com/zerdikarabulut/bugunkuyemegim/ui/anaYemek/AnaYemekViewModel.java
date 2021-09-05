package com.zerdikarabulut.bugunkuyemegim.ui.anaYemek;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AnaYemekViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AnaYemekViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}