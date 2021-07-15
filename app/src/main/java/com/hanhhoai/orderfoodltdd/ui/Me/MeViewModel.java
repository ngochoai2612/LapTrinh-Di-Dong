package com.hanhhoai.orderfoodltdd.ui.Me;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MeViewModel extends ViewModel {

    private final MutableLiveData<String> Text;

    public MeViewModel() {
        Text = new MutableLiveData<>();
        Text.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return Text;
    }
}