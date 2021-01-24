package com.example.whatsappclone.interfafaces;

import com.example.whatsappclone.model.chat.chat;

import java.util.List;

public interface OnReadChatCallback {

    void onReadSuccess(List<chat> list);
    void onReadFailed();

}
