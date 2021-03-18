package com.bkav.demochat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChatListFragment extends Fragment {

    private RecyclerView mChatRecyclerView;
    private ChatListAdapter mChatListAdapter;
    private ArrayList<User> mUserList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.chat_list_fragment, container, false);

        mUserList = new ArrayList<>();

        initUserList();
        mChatRecyclerView = root.findViewById(R.id.list_chat);
        mChatListAdapter = new ChatListAdapter(getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mChatRecyclerView.setLayoutManager(linearLayoutManager);
        mChatRecyclerView.setAdapter(mChatListAdapter);
        mChatListAdapter.updateChatList(mUserList);

        return root;
    }

    public void initUserList(){
        User user1 = new User(1,"Anh Tien","abc",true);
        User user2 = new User(2,"Van Tien","def",false);
        User user3 = new User(3,"Anh Huy","ghi",true);
        mUserList.add(user1);
        mUserList.add(user2);
        mUserList.add(user3);
    }
}
