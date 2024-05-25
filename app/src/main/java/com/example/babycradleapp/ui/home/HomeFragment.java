package com.example.babycradleapp.ui.home;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.babycradleapp.R;
import com.example.babycradleapp.databinding.FragmentHomeBinding;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;
    private LineChart lineChart;
    private TextView humidityTextView;
    private TextView temperatureTextView;
    private TextView babyNameTextView;
    private String name="-";
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        lineChart = binding.lineChart;
        humidityTextView = binding.valueHumidity;
        temperatureTextView = binding.valueTemperature;
        babyNameTextView = binding.textViewBabyName;
        setupLineChart();
        getBabyName();
        homeViewModel.getHumidityEntries().observe(getViewLifecycleOwner(), new Observer<List<Entry>>() {
            @Override
            public void onChanged(List<Entry> humidityEntries) {
                updateLineChart(humidityEntries, homeViewModel.getTemperatureEntries().getValue());
                updateLastValues();
            }
        });

        homeViewModel.getTemperatureEntries().observe(getViewLifecycleOwner(), new Observer<List<Entry>>() {
            @Override
            public void onChanged(List<Entry> temperatureEntries) {
                updateLineChart(homeViewModel.getHumidityEntries().getValue(), temperatureEntries);
                updateLastValues();
            }
        });

        return root;
    }

    private void setupLineChart() {
        lineChart.setTouchEnabled(true);
        lineChart.setPinchZoom(true);

        Description description = new Description();
        description.setText("Temperature and Humidity Over Time");
        lineChart.setDescription(description);

        // Customize legend
        Legend legend = lineChart.getLegend();
        legend.setForm(Legend.LegendForm.SQUARE);
        legend.setFormSize(10f); // set the size of the legend forms
        legend.setFormToTextSpace(10f); // space between the legend forms and the text
        legend.setXEntrySpace(15f); // space between the legend entries

        // Set the colors for the legend
        int humidityColor = ContextCompat.getColor(requireContext(), R.color.blue); // customize color
        int temperatureColor = ContextCompat.getColor(requireContext(), R.color.pink); // customize color
        legend.setTextColor(ContextCompat.getColor(requireContext(), R.color.black)); // customize color

        // Set the legend entries colors
        //legend.setCustom(new int[]{humidityColor, temperatureColor}, new String[]{"Humidity", "Temperature"});
    }

    private void updateLineChart(List<Entry> humidityEntries, List<Entry> temperatureEntries) {
        if (humidityEntries == null || temperatureEntries == null) {
            return; // wait until both data sets are available
        }

        int humidityColor = ContextCompat.getColor(requireContext(), R.color.blue); // customize color
        int temperatureColor = ContextCompat.getColor(requireContext(), R.color.pink); // customize color

        LineDataSet humidityDataSet = new LineDataSet(humidityEntries, "Humidity");
        humidityDataSet.setColor(humidityColor); // customize color
        humidityDataSet.setLineWidth(2f);
        humidityDataSet.setValueTextColor(humidityColor); // customize value text color

        LineDataSet temperatureDataSet = new LineDataSet(temperatureEntries, "Temperature");
        temperatureDataSet.setColor(temperatureColor); // customize color
        temperatureDataSet.setLineWidth(2f);
        temperatureDataSet.setValueTextColor(temperatureColor); // customize value text color

        LineData lineData = new LineData(humidityDataSet, temperatureDataSet);
        lineChart.setData(lineData);
        lineChart.invalidate(); // refresh the chart
    }

    private void updateLastValues() {
        List<Entry> humidityEntries = homeViewModel.getHumidityEntries().getValue();
        List<Entry> temperatureEntries = homeViewModel.getTemperatureEntries().getValue();

        if (humidityEntries != null && !humidityEntries.isEmpty()) {
            float lastHumidityValue = humidityEntries.get(humidityEntries.size() - 1).getY();
            String text=String.valueOf(lastHumidityValue)+"%";
            humidityTextView.setText(text);
        }

        if (temperatureEntries != null && !temperatureEntries.isEmpty()) {
            float lastTemperatureValue = temperatureEntries.get(temperatureEntries.size() - 1).getY();
            String text=String.valueOf(lastTemperatureValue)+"ºC";
            temperatureTextView.setText(text);
        }
    }

    private void updateName(){
        babyNameTextView.setText(name);
    }
    public void getBabyName(){
        System.out.println("----Getting Baby name!!-----");
        FirebaseFirestore.getInstance()
                .collection("baby_info")
                .document("nom")
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Retrieve the baby name from Firestore
                        String babyName = documentSnapshot.getString("name");
                        if (babyName != null) {
                            name = babyName; // Assign the retrieved name to the 'name' variable
                            updateName();
                        } else {
                            Log.d(TAG, "Error: baby name is null.");
                        }
                    } else {
                        Log.d(TAG, "Error: Document does not exist.");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error getting baby name.", e);
                });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
