
package com.example.whatsappclone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.whatsappclone.R;
import com.example.whatsappclone.Tool.AudioService;
import com.example.whatsappclone.model.chat.chat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class chatsadapter extends RecyclerView.Adapter<chatsadapter.viewholder> {

    private List<chat> list;
    private Context context;

    public static final int msg_type_left = 0;
    public static final int msg_type_right = 1;
    private FirebaseUser firebaseUser;
    private ImageButton tmpBtnPlay;
    private AudioService audioService;


    public chatsadapter(List<chat> list, Context context) {
        this.list = list;
        this.context = context;
        this.audioService = new AudioService(context);
    }

    public void setList(List<chat> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType==msg_type_left) {
            view = LayoutInflater.from(context).inflate(R.layout.layout_chat_left, parent, false);
        }
        else{
            view = LayoutInflater.from(context).inflate(R.layout.layout_chat_right, parent, false);
        }
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        holder.bind(list.get(position));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {

        private TextView textmessage;
        private LinearLayout layoutText, layoutImage, layoutVoice;
        private ImageView imageMessage;
        private ImageButton btnPlay;
        private viewholder tmpHolder;
        public viewholder(@NonNull View itemView) {
            super(itemView);

            textmessage = itemView.findViewById(R.id.text_message);
            layoutImage = itemView.findViewById(R.id.layout_iamge);
            layoutText = itemView.findViewById(R.id.layout_text);
            imageMessage = itemView.findViewById(R.id.iamge_chat);
            layoutVoice = itemView.findViewById(R.id.layout_voice);
            btnPlay = itemView.findViewById(R.id.btn_play_chat);
        }
        void bind(final chat chats) {
           // textmessage.setText(chats.getTextMessage());
            switch (chats.getType()) {
                case "TEXT":
                    layoutText.setVisibility(View.VISIBLE);
                    layoutImage.setVisibility(View.GONE);
                    layoutVoice.setVisibility(View.GONE);

                    textmessage.setText(chats.getTextMessage());

                    break;
                case "IMAGE":
                    layoutText.setVisibility(View.GONE);
                    layoutImage.setVisibility(View.VISIBLE);
                    layoutVoice.setVisibility(View.GONE);

                    Glide.with(context).load(chats.getUrl()).into(imageMessage);
                    break;
                case "VOICE":
                    layoutText.setVisibility(View.GONE);
                    layoutImage.setVisibility(View.GONE);
                    layoutVoice.setVisibility(View.VISIBLE);

                    layoutVoice.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (tmpBtnPlay != null) {
                                tmpBtnPlay.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_play_circle_filled_24));
                            }

                            btnPlay.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_pause_circle_filled_24));

                            audioService.playAudioFromUrl(chats.getUrl(), new AudioService.OnPlayCallBack() {
                                @Override
                                public void onFinished() {
                                    btnPlay.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_play_circle_filled_24));
                                }
                            });

                            tmpBtnPlay = btnPlay;

                        }
                    });

                    break;
                }

        }
    }


    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(list.get(position).getSender().equals(firebaseUser.getUid())) {

            return  msg_type_right;
        }
        else{
            return msg_type_left;
        }
       
    }
}
