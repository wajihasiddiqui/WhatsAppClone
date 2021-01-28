package com.example.whatsappclone.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.whatsappclone.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jsibbold.zoomage.ZoomageView;

import java.util.Objects;

public class DialogReviewSendImage {

    private Context context;
    private Dialog dialog;
    private Bitmap bitmap;
    private ZoomageView image;
    private FloatingActionButton btnsend;

    public DialogReviewSendImage(Context context, Bitmap bitmap) {
        this.context = context;
        this.dialog = new Dialog(context);
        this.bitmap = bitmap;

        initialize();
    }

    public void initialize(){

        dialog.requestWindowFeature(Window.FEATURE_ACTION_BAR);
        dialog.setContentView(R.layout.activity_review_send_image);

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(params);

        image = dialog.findViewById(R.id.imageView);
        btnsend = dialog.findViewById(R.id.btn_send);
    }

    public void show(final OnCallBack onCallBack){
        dialog.show();
        image.setImageBitmap(bitmap);
        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCallBack.onButtonSendClick();
                dialog.dismiss();
            }
        });
    }

    public interface  OnCallBack{
        void onButtonSendClick();
    }

}
