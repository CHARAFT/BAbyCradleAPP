package com.example.babycradleapp.ui.events;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class EventViewModel extends ViewModel {
    private final MutableLiveData<List<Event>> eventsLiveData = new MutableLiveData<>();
    private final FirebaseFirestore db;

    public EventViewModel() {
        db = FirebaseFirestore.getInstance();
        loadEvents();
    }

    public LiveData<List<Event>> getEventsLiveData() {
        return eventsLiveData;
    }

    public void loadEvents() {
        db.collection("Event").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Event> eventList = new ArrayList<>();
                        for (DocumentSnapshot snapshot : task.getResult()) {
                            Event event = new Event(
                                    snapshot.getString("id"),
                                    snapshot.getString("title"),
                                    snapshot.getString("desc"),
                                    snapshot.getString("date")
                            );
                            eventList.add(event);
                        }
                        eventsLiveData.postValue(eventList); // Use postValue for background threads
                    } else {
                        // Handle the error case
                        // For example, you can log the error or show a toast
                        Log.e("EventViewModel", "Error loading events", task.getException());
                    }
                });
    }

    public void filterEvents(String query) {
        List<Event> currentEvents = eventsLiveData.getValue();
        if (currentEvents == null) return;

        List<Event> filteredList = new ArrayList<>();
        for (Event event : currentEvents) {
            if (event.getTitle() != null && event.getTitle().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(event);
            }
        }
        eventsLiveData.setValue(filteredList); // Update LiveData with filtered list
    }
}
