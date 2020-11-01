package com.example.whatsappclone.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.whatsappclone.R;
import com.example.whatsappclone.model.calllist;
import com.example.whatsappclone.model.chatlist;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

public class calllistadapter extends RecyclerView.Adapter<calllistadapter.Holder>{

    private List<calllist> list;
    private Context context;

    public calllistadapter(List<calllist> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_call_list,parent,false);
        return new Holder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        calllist calllists = list.get(position);

        holder.txname.setText(calllists.getUserName());
        holder.txdate.setText(calllists.getDate());

        if(calllists.getCallType().equals("missed")){
            holder.arrow.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_call_24));
            holder.arrow.getDrawable().setTint(context.getResources().getColor(android.R.color.holo_green_light));
        }
        else if(calllists.getCallType().equals("income")){
            holder.arrow.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_call_24));
            holder.arrow.getDrawable().setTint(context.getResources().getColor(android.R.color.holo_blue_dark));
        }
        else{
            holder.arrow.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_call_24));
            holder.arrow.getDrawable().setTint(context.getResources().getColor(android.R.color.holo_blue_dark));
        }

        Glide.with(context).load(calllists.getUrlProfile()).into(holder.profile);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private TextView txname,txdate;
        private CircularImageView profile;
        private ImageView arrow;

        public Holder(@NonNull View itemView) {
            super(itemView);
            txdate = itemView.findViewById(R.id.txdate);
            txname = itemView.findViewById(R.id.txname);
            profile = itemView.findViewById(R.id.profile);
            arrow = itemView.findViewById(R.id.imgarrow);

        }
    }
}
