package com.example.whatsappclone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.whatsappclone.R;
import com.example.whatsappclone.model.chatlist;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

public class chatlistadapter extends RecyclerView.Adapter<chatlistadapter.Holder>{

    private List<chatlist> list;
    private Context context;

    public chatlistadapter(List<chatlist> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_chat_list,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        chatlist chatlists = list.get(position);

        holder.txname.setText(chatlists.getUsername());
        holder.txdesc.setText(chatlists.getDescription());
        holder.txdate.setText(chatlists.getDate());

        Glide.with(context).load(chatlists.getUrlprofile()).into(holder.profile);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private TextView txname,txdesc,txdate;
        private CircularImageView profile;

        public Holder(@NonNull View itemView) {
            super(itemView);
            txdate = itemView.findViewById(R.id.txdate);
            txname = itemView.findViewById(R.id.txname);
            txdesc = itemView.findViewById(R.id.txdesc);
            profile = itemView.findViewById(R.id.profile);

        }
    }
}
