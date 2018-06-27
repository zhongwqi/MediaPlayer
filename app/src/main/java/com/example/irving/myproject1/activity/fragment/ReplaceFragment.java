package com.example.irving.myproject1.activity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.irving.myproject1.activity.base.BasePager;

import java.util.ArrayList;

/**
 * Created by Irving on 2017/10/24.
 */

public class ReplaceFragment extends Fragment {
    private BasePager basePager;
    public ReplaceFragment(BasePager basePager) {
        this.basePager = basePager;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        BasePager basePager = this.basePager;
        if(basePager != null) {
            return basePager.rootView;
        }
        return null;
    }


}
