package com.example.babycradleapp;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.babycradleapp.adapter.MyAdapter;
import com.example.babycradleapp.model.Article;

import java.util.LinkedList;

public class articleFragment extends Fragment {

    private ArticleViewModel mViewModel;
    private RecyclerView recyclerView;
    private MyAdapter adapter;

    public static articleFragment newInstance() {
        return new articleFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_article, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MyAdapter(new LinkedList<>(), getContext());
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ArticleViewModel.class);
        mViewModel.getArticles().observe(getViewLifecycleOwner(), articles -> {
            adapter.setArticles(articles);
        });
    }
}
