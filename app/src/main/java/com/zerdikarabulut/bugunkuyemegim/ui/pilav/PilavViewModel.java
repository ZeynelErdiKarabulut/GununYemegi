package com.zerdikarabulut.bugunkuyemegim.ui.pilav;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PilavViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public PilavViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}