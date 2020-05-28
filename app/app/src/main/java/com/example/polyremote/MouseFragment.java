package com.example.polyremote;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.polyremote.databinding.FragmentMouseBinding;


public class MouseFragment extends Fragment {

    private FragmentMouseBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMouseBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}
