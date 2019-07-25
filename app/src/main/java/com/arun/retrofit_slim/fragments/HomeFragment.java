package com.arun.retrofit_slim.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arun.retrofit_slim.R;
import com.arun.retrofit_slim.storage.SharedPrefManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    TextView txtEmail, txtName, txtSchool;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtEmail = view.findViewById(R.id.txtEmail);
        txtName = view.findViewById(R.id.txtName);
        txtSchool = view.findViewById(R.id.txtSchool);

        txtEmail.setText(SharedPrefManager.getInstance(getActivity()).getUser().getEmail());
        txtName.setText(SharedPrefManager.getInstance(getActivity()).getUser().getName());
        txtSchool.setText(SharedPrefManager.getInstance(getActivity()).getUser().getSchool());
    }
}
