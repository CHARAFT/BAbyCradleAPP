package com.example.babycradleapp.ui.home;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.github.mikephil.charting.data.Entry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<List<Entry>> humidityEntries;
    private final MutableLiveData<List<Entry>> temperatureEntries;
    private final DatabaseReference databaseReference;

    public HomeViewModel() {
        humidityEntries = new MutableLiveData<>();
        temperatureEntries = new MutableLiveData<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Datas");

        fetchData();
    }

    public LiveData<List<Entry>> getHumidityEntries() {
        return humidityEntries;
    }

    public LiveData<List<Entry>> getTemperatureEntries() {
        return temperatureEntries;
    }


    private void fetchData() {
        DatabaseReference humiditeRef = databaseReference.child("Humidite");
        DatabaseReference temperatureRef = databaseReference.child("Temperature");

        ValueEventListener humiditeListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Entry> entries = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String timestampStr = dataSnapshot.child("timestamp").getValue(String.class);
                    long timestamp = parseTimestamp(timestampStr);
                    float value = dataSnapshot.child("value").getValue(Float.class);
                    entries.add(new Entry(timestamp, value));
                }
                humidityEntries.setValue(entries);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        };

        ValueEventListener temperatureListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Entry> entries = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String timestampStr = dataSnapshot.child("timestamp").getValue(String.class);
                    long timestamp = parseTimestamp(timestampStr);
                    float value = dataSnapshot.child("value").getValue(Float.class);
                    entries.add(new Entry(timestamp, value));
                }
                temperatureEntries.setValue(entries);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        };

        humiditeRef.addValueEventListener(humiditeListener);
        temperatureRef.addValueEventListener(temperatureListener);
    }

    private long parseTimestamp(String timestampStr) {
        // Implement your timestamp parsing logic here
        // For example, you can parse the timestamp string to a long value
        // Assume the timestamp string is in the format "2024-05-24T11:42:27.616Z"
        // You can use SimpleDateFormat or other methods to parse the date string

        // Here is an example using SimpleDateFormat
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        try {
            Date date = sdf.parse(timestampStr);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
