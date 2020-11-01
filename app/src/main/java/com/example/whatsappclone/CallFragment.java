package com.example.whatsappclone;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.whatsappclone.adapter.calllistadapter;
import com.example.whatsappclone.adapter.chatlistadapter;
import com.example.whatsappclone.model.calllist;

import java.util.ArrayList;
import java.util.List;


public class
CallFragment extends Fragment {


    public CallFragment() {
        // Required empty public constructor
    }

    private List<calllist> list = new ArrayList();
    private RecyclerView recyclerview;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_call, container, false);

        recyclerview = view.findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));

        getCallList();

        return view;
    }

    private void getCallList() {
        list.add(new calllist("001","wajiha","11/5/2020","https://tse3.mm.bing.net/th?id=OIP.Xa_FUFxWwz48najD3i5uCAHaFY&pid=Api&P=0&w=254&h=185","income"));
        list.add(new calllist("002","wajiha","11/5/2020","https://tse3.mm.bing.net/th?id=OIP.Xa_FUFxWwz48najD3i5uCAHaFY&pid=Api&P=0&w=254&h=185","missed"));
        list.add(new calllist("003","wajiha","11/5/2020","https://tse3.mm.bing.net/th?id=OIP.Xa_FUFxWwz48najD3i5uCAHaFY&pid=Api&P=0&w=254&h=185","income"));


        recyclerview.setAdapter(new calllistadapter(list,getContext()));

    }
}