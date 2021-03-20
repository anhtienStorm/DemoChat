package com.bkav.demochat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ChatListFragment extends ListFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mItemResource = R.layout.user_item_view;
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
        User user1 = new User(1,"Anh Tien","abc",true);
        User user2 = new User(2,"Van Tien","def",false);
        User user3 = new User(3,"Anh Huy","ghi",true);
        mUserList.add(user1);
        mUserList.add(user2);
        mUserList.add(user3);
    }
}
