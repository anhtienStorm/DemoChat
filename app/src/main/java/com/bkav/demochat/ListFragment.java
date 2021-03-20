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

public class ListFragment extends Fragment {

    protected RecyclerView mChatRecyclerView;
    protected ChatListAdapter mChatListAdapter;
    protected ArrayList<User> mUserList = new ArrayList<>();
    protected int mItemResource;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.chat_list_fragment, container, false);

        mChatRecyclerView = root.findViewById(R.id.list_chat);
        mChatListAdapter = new ChatListAdapter(getContext(), mItemResource);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mChatRecyclerView.setLayoutManager(linearLayoutManager);
        mChatRecyclerView.setAdapter(mChatListAdapter);

        return root;
    }
}
