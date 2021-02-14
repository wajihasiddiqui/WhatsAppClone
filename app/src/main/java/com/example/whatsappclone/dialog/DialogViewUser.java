package com.example.whatsappclone.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityOptionsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.example.whatsappclone.R;
import com.example.whatsappclone.adapter.chatlistadapter;
import com.example.whatsappclone.chats.ChatsActivity;
import com.example.whatsappclone.common.common;
import com.example.whatsappclone.display.ViewImageActivity;
import com.example.whatsappclone.model.chatlist;
import com.example.whatsappclone.profile.User_ProfileActivity;
import com.google.android.gms.common.internal.service.Common;

import java.util.Objects;

public class DialogViewUser {

    private Context context;

    public DialogViewUser(Context context, chatlist chatlist) {
        this.context = context;
        initialize(chatlist);
    }
    public void initialize(final chatlist chatlist){

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_ACTION_BAR); // before
        dialog.setContentView(R.layout.dialog_view_user);

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);

        ImageButton btnChat, btnCall, btnVideoCall, btnInfo;
        final ImageView profile;
        TextView userName;

        btnChat = dialog.findViewById(R.id.btn_chat);
        btnCall = dialog.findViewById(R.id.btn_call);
        btnVideoCall = dialog.findViewById(R.id.btn_video);
        btnInfo = dialog.findViewById(R.id.btn_info);

        profile = dialog.findViewById(R.id.image_profile);
        userName = dialog.findViewById(R.id.tv_username);

        userName.setText(chatlist.getUsername());
        Glide.with(context).load(chatlist.getUrlprofile()).into(profile);

        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ChatsActivity.class)
                        .putExtra("userID",chatlist.getUserId())
                        .putExtra("userName",chatlist.getUsername())
                        .putExtra("userProfile",chatlist.getUrlprofile()));
                dialog.dismiss();
            }
        });
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Call Clicked",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        btnVideoCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Video Call Clicked",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, User_ProfileActivity.class)
                        .putExtra("userID",chatlist.getUserId())
                        .putExtra("userProfile",chatlist.getUrlprofile())
                        .putExtra("userName",chatlist.getUsername()));
                dialog.dismiss();
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profile.invalidate();
                Drawable dr = profile.getDrawable();
                common.IMAGE_BITMAP = ((GlideBitmapDrawable)dr.getCurrent()).getBitmap();
                ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, profile, "image");
                Intent intent = new Intent(context, ViewImageActivity.class);
                context.startActivity(intent, activityOptionsCompat.toBundle());

            }
        });


        dialog.show();
    }
}
