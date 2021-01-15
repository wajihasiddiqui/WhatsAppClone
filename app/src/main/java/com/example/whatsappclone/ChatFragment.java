package com.example.whatsappclone;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.whatsappclone.adapter.chatlistadapter;
import com.example.whatsappclone.model.chatlist;

import java.util.ArrayList;
import java.util.List;


public class ChatFragment extends Fragment {



    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        List<chatlist> list = new ArrayList<>();

        RecyclerView recyclerview = view.findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));


//      recyclerview.setAdapter(new chatlistadapter(list,getContext()));
        return view;
    }

}