package com.zerdikarabulut.bugunkuyemegim.ui.salata;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SalataViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SalataViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is share fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}