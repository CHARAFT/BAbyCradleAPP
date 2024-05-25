package com.example.babycradleapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.babycradleapp.model.Article;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ArticleViewModel extends ViewModel {
    private final MutableLiveData<List<Article>> articles;

    public ArticleViewModel() {
        articles = new MutableLiveData<>();
        loadArticles();
    }

    public LiveData<List<Article>> getArticles() {
        return articles;
    }

    private void loadArticles() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("articles")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Article> articleList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Article article = document.toObject(Article.class);
                            articleList.add(article);
                        }
                        articles.setValue(articleList);
                    } else {
                        // Handle error
                    }
                });
    }
}
