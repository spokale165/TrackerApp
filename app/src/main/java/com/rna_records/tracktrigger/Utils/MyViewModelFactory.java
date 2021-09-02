package com.rna_records.tracktrigger.Utils;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.rna_records.tracktrigger.TrackerItems.TrackerItemsViewModel;

public class MyViewModelFactory implements ViewModelProvider.Factory {
    private String mParam;


    public MyViewModelFactory(String param) {
        mParam = param;
    }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new TrackerItemsViewModel(mParam);
    }
}