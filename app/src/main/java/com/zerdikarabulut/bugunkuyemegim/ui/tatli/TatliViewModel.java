package com.zerdikarabulut.bugunkuyemegim.ui.tatli;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TatliViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public TatliViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is send fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}