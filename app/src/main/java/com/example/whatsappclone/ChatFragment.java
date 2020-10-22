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

    private List<chatlist> list = new ArrayList();
    private RecyclerView recyclerview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        recyclerview = view.findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));

        getChatList();
        return view;
    }

    private void getChatList() {
        list.add(new chatlist("11", "wajiha","hello","15/04/2020","https://tse3.mm.bing.net/th?id=OIP.Xa_FUFxWwz48najD3i5uCAHaFY&pid=Api&P=0&w=254&h=185"));
        list.add(new chatlist("12", "wajiha","hello","15/04/2020","https://tse1.mm.bing.net/th?id=OIP.r8soQNgKeW77Is5e_fCUCQHaHa&pid=Api&P=0&w=300&h=300"));
        list.add(new chatlist("13", "wajiha","hello","15/04/2020","https://tse1.mm.bing.net/th?id=OIP.11S6ygRSOPAQ_67To_nW3AHaEH&pid=Api&P=0&w=286&h=160"));
        list.add(new chatlist("14", "wajiha","hello","15/04/2020","https://tse3.mm.bing.net/th?id=OIP.eTCKYXrmICrgAr1f93GxPQHaFi&pid=Api&P=0&w=242&h=182"));

        recyclerview.setAdapter(new chatlistadapter(list,getContext()));

    }


}