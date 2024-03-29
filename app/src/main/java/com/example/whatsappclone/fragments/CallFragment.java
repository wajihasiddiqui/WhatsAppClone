package com.example.whatsappclone.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.whatsappclone.R;
import com.example.whatsappclone.adapter.calllistadapter;
import com.example.whatsappclone.adapter.chatlistadapter;
import com.example.whatsappclone.model.calllist;

import java.util.ArrayList;
import java.util.List;


public class CallFragment extends Fragment {


    public CallFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_call, container, false);

        RecyclerView recyclerview = view.findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));

        List<calllist> list = new ArrayList<>();
//        recyclerview.setAdapter(new calllistadapter(list,getContext()));

        return view;
    }

}