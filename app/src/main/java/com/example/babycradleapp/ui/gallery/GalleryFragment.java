package com.example.babycradleapp.ui.gallery;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.babycradleapp.R;
import com.example.babycradleapp.databinding.FragmentGalleryBinding;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import Models.babyinfo;

public class GalleryFragment extends Fragment {
    private EditText editTextWeight, editTextSleepDuration, editTextBreastfeedingRate, editTextLength;
    private FirebaseFirestore db;
    private FragmentGalleryBinding binding;
    private LineChart sleepDurationChart;

    //
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        editTextWeight = root.findViewById(R.id.editTextWeight);
        editTextSleepDuration = root.findViewById(R.id.editTextSleepDuration);
        editTextBreastfeedingRate = root.findViewById(R.id.editTextBreastfeedingRate);
        editTextLength = root.findViewById(R.id.editTextLength);

        Button buttonSave = root.findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(v -> saveBabyInfo());

        LineChart weightChart = root.findViewById(R.id.graphLayout);
        sleepDurationChart = root.findViewById(R.id.sleep_duration);
        BarChart lengthcart= root.findViewById(R.id.lengthChart);
        HorizontalBarChart breast = root.findViewById(R.id.aliChart);

        galleryViewModel.getBabyInfoList().observe(getViewLifecycleOwner(), babyInfoList -> {
            setupWeightChart(weightChart, babyInfoList);
        });

        galleryViewModel.getSleepDurationList().observe(getViewLifecycleOwner(), durations -> {
            updateSleepDurationChart(sleepDurationChart, durations);
        });
        galleryViewModel.getLengthList().observe(getViewLifecycleOwner(), lengths -> {
            // Mettre à jour le graphique de longueur avec les nouvelles données
            updateLengthChart(lengthcart,lengths);
        });
        galleryViewModel.getbeastList().observe(getViewLifecycleOwner(), Rates -> {
            updateBreastfeedingRatesChart(breast,Rates);
        });
        return root;



    }
    private void setupWeightChart(LineChart chart, List<babyinfo> babyInfoList) {
        List<Entry> entries = new ArrayList<>();

        // Ajoutez les données de poids à la liste d'entrées
        for (int i = 0; i < babyInfoList.size(); i++) {
            String weightStr = babyInfoList.get(i).getWeight();
            if (weightStr != null && !weightStr.trim().isEmpty()) { // Vérifier si la chaîne n'est pas nulle ou vide
                float weight = Float.parseFloat(weightStr.trim()); // Convertir la chaîne en flottant
                entries.add(new Entry(i, weight));
            }
        }

        LineDataSet dataSet = new LineDataSet(entries, "Évolution du poids");
        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);

        // Personnalisez l'apparence du graphique (facultatif)
        dataSet.setColor(Color.BLUE);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setCircleColor(Color.BLUE);
        dataSet.setLineWidth(2f);

        // Mettez à jour le graphique
        chart.invalidate();
    }

    private void saveBabyInfo() {
        GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);
        String weight = editTextWeight.getText().toString();
        String sleepDuration = editTextSleepDuration.getText().toString();
        String breastfeedingRate = editTextBreastfeedingRate.getText().toString();
        String length = editTextLength.getText().toString();

        galleryViewModel.saveBabyInfo(weight, sleepDuration, breastfeedingRate, length);
    }
    private void updateSleepDurationChart(LineChart sleepDurationChar, List<Float> durations) {
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < durations.size(); i++) {
            entries.add(new Entry(i, durations.get(i)));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Durée de sommeil");
        LineData lineData = new LineData(dataSet);

        // Personnalisez l'apparence du graphique comme vous le souhaitez

        sleepDurationChar.setData(lineData);
        sleepDurationChar.invalidate(); // Mettre à jour le graphique
    }
    private void updateLengthChart(BarChart chart, List<Float> lengths) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < lengths.size(); i++) {
            entries.add(new BarEntry(i, lengths.get(i)));
        }

        BarDataSet dataSet = new BarDataSet(entries, "Longueur");
        BarData barData = new BarData(dataSet);

        // Personnalisez l'apparence du graphique (couleur, largeur de la barre, etc.)
        dataSet.setColor(Color.BLUE);

        // Mettez à jour le graphique avec les nouvelles données
        chart.setData(barData);
        chart.invalidate();
    }
    private void updateBreastfeedingRatesChart(HorizontalBarChart Chart, List<Float> rates) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < rates.size(); i++) {
            entries.add(new BarEntry(i, rates.get(i)));
        }

        BarDataSet dataSet = new BarDataSet(entries, "Taux d'allaitement");
        dataSet.setColor(Color.BLUE);
        dataSet.setValueTextColor(Color.BLACK);

        BarData barData = new BarData(dataSet);
        Chart.setData(barData);


        Chart.invalidate();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}