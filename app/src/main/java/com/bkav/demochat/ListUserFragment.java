package com.bkav.demochat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ListUserFragment extends ListFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mItemResource = R.layout.user_online_item_view;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        initUserList();
        mChatListAdapter.updateChatList(mUserList);
    }

    private void initUserList(){
        mUserList.clear();
        User user1 = new User(1,"Anh Tien","",true);
        User user2 = new User(3,"Anh Huy","",true);
        mUserList.add(user1);
        mUserList.add(user2);
    }
}
