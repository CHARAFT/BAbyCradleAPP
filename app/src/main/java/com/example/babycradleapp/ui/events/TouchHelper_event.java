package com.example.babycradleapp.ui.events;

import android.graphics.Canvas;
import android.graphics.Color;
import android.widget.ExpandableListView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.example.babycradleapp.R;



public class TouchHelper_event extends ItemTouchHelper.SimpleCallback {
    private MyAdapter_event adapter;

    public TouchHelper_event(MyAdapter_event adapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapter = adapter;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        final int position = viewHolder.getAdapterPosition();
        if (direction == ItemTouchHelper.LEFT) {
            adapter.updateData(position); // Edit action
        } else if (direction == ItemTouchHelper.RIGHT) {
            adapter.deleteData(position); // Delete action
        }
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

    }
}