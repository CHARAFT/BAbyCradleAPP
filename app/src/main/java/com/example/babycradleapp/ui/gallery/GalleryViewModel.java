package com.example.babycradleapp.ui.gallery;

import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import Models.babyinfo;

public class GalleryViewModel extends ViewModel {
    private FirebaseFirestore db;
    private MutableLiveData<List<babyinfo>> babyInfoList;
    private MutableLiveData<List<Float>> sleepDurationList;
    private MutableLiveData<List<Float>> lengthList;
    private MutableLiveData<List<Float>> breastfeedingRateList;
    public GalleryViewModel() {
        db = FirebaseFirestore.getInstance();
        babyInfoList = new MutableLiveData<>();
        fetchBabyInfoList();
        sleepDurationList = new MutableLiveData<>();
        fetchSleepDurationList();
        lengthList = new MutableLiveData<>();
        fetchLengthList();
        breastfeedingRateList = new MutableLiveData<>();
        fetchBreastfeedingRates(); // Appel de la méthode pour récupérer les taux d'allaitement

    }

    public void saveBabyInfo(String weight, String sleepDuration, String breastfeedingRate, String length) {
        babyinfo babyInfo = new babyinfo(weight, sleepDuration, breastfeedingRate, length);
        db.collection("baby_info")
                .add(babyInfo)
                .addOnSuccessListener(documentReference -> {
                    // Gérer le succès de l'enregistrement
                })
                .addOnFailureListener(e -> {
                    // Gérer l'échec de l'enregistrement
                });
    }

    public LiveData<List<babyinfo>> getBabyInfoList() {
        return babyInfoList;
    }
    public LiveData<List<Float>> getSleepDurationList() {
        return sleepDurationList;
    }
    public LiveData<List<Float>> getLengthList() {
        return lengthList;
    }
    public LiveData<List<Float>> getbeastList() {
        return breastfeedingRateList ;
    }
    private void fetchBabyInfoList() {
        db.collection("baby_info")
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        // Gérer les erreurs
                        return;
                    }

                    List<babyinfo> list = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        babyinfo babyInfo = doc.toObject(babyinfo.class);
                        list.add(babyInfo);
                    }
                    babyInfoList.setValue(list);
                });
    }


    private void fetchSleepDurationList() {
        db.collection("baby_info")
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        // Gérer les erreurs
                        return;
                    }

                    List<Float> durations = new ArrayList<>();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        String durationStr = documentSnapshot.getString("sleepDuration");
                        if (durationStr != null && !durationStr.isEmpty()) {
                            float duration = Float.parseFloat(durationStr);
                            durations.add(duration);
                        }
                    }
                    sleepDurationList.setValue(durations);
                });
    }
    private void fetchBreastfeedingRates(){
        db.collection("baby_info")
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        // Gérer les erreurs
                        return;
                    }

                    List<Float> Rates = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        String brStr = doc.getString("breastfeedingRate");
                        if (brStr != null && !brStr.isEmpty()) {
                            try {
                                Float length = Float.parseFloat(brStr);
                                Rates.add(length);
                            } catch (NumberFormatException ex) {
                                // Gérer les cas où la conversion échoue
                                ex.printStackTrace();
                            }
                        }
                    }
                    breastfeedingRateList.setValue(Rates);
                });
    }

    private void fetchLengthList() {
        db.collection("baby_info")
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        // Gérer les erreurs
                        return;
                    }

                    List<Float> lengths = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        String lengthStr = doc.getString("length");
                        if (lengthStr != null && !lengthStr.isEmpty()) {
                            try {
                                Float length = Float.parseFloat(lengthStr);
                                lengths.add(length);
                            } catch (NumberFormatException ex) {
                                // Gérer les cas où la conversion échoue
                                ex.printStackTrace();
                            }
                        }
                    }
                    lengthList.setValue(lengths);
                });
    }

}
