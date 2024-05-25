package com.example.babycradleapp.ui.events;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.babycradleapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class MyAdapter_event extends RecyclerView.Adapter<MyAdapter_event.MyViewHolder> {
    private Fragment fragment;
    private List<Event> mlist;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private EventViewModel eventViewModel;

    public MyAdapter_event(Fragment fragment, List<Event> mlist, EventViewModel eventViewModel) {
        this.fragment = fragment;
        this.mlist = mlist;
        this.eventViewModel = eventViewModel;
    }

    public void updateEventList(List<Event> newEventList) {
        this.mlist = newEventList;
        notifyDataSetChanged();
    }

    public void setFilteredList(List<Event> filteredList) {
        this.mlist = filteredList;
        notifyDataSetChanged();
    }

    public void updateData(int position) {
        Event item_event = mlist.get(position);
        Bundle bundle = new Bundle();
        bundle.putString("uId", item_event.getId());
        bundle.putString("uTitle", item_event.getTitle());
        bundle.putString("uDesc", item_event.getDesc());
        bundle.putString("uDate", item_event.getDate());

        AddEventFragment addEventFragment = new AddEventFragment();
        addEventFragment.setArguments(bundle);

        FragmentManager fragmentManager = fragment.getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, addEventFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void deleteData(int position) {
        Event item_event = mlist.get(position);
        db.collection("Event").document(item_event.getId()).delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        notifyRemoved(position);
                        Toast.makeText(fragment.getContext(), "Data Deleted !!", Toast.LENGTH_SHORT).show();
                        // Notify the ViewModel to refresh the data
                        eventViewModel.loadEvents();
                    } else {
                        Toast.makeText(fragment.getContext(), "Error !!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void notifyRemoved(int position) {
        mlist.remove(position);
        notifyItemRemoved(position);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(fragment.getContext()).inflate(R.layout.item_event, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Event event = mlist.get(position);
        holder.title.setText(event.getTitle());
        holder.desc.setText(event.getDesc());
        holder.date.setText(event.getDate());
    }

    @Override
    public int getItemCount() {
        return mlist != null ? mlist.size() : 0;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, desc, date;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title_text);
            desc = itemView.findViewById(R.id.desc_text);
            date = itemView.findViewById(R.id.date_text);
        }
    }
}