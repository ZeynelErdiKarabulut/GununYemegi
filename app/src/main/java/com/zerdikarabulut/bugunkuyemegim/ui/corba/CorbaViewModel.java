package com.zerdikarabulut.bugunkuyemegim.ui.corba;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CorbaViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CorbaViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is tools fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}