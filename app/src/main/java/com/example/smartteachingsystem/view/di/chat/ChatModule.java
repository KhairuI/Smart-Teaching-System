package com.example.smartteachingsystem.view.di.chat;

import com.bumptech.glide.RequestManager;
import com.example.smartteachingsystem.view.adapter.ChatHistoryAdapter;
import com.example.smartteachingsystem.view.adapter.TeacherChatHistoryAdapter;

import dagger.Module;
import dagger.Provides;

@Module
public class ChatModule {

    @Provides
    static ChatHistoryAdapter provideAllTeacherAdapter(RequestManager requestManager){
        return new ChatHistoryAdapter(requestManager);
    }

    @Provides
    static TeacherChatHistoryAdapter provideTeacherChatHistoryAdapter(RequestManager requestManager){
        return new TeacherChatHistoryAdapter(requestManager);
    }

}
