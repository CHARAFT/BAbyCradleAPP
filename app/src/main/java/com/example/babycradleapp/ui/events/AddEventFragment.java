package com.example.babycradleapp.ui.events;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.babycradleapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddEventFragment extends Fragment {

    private EditText mTitle, mDesc, mDate;
    private Button mSaveBtn, mShowBtn;
    private FirebaseFirestore db;
    private String uTitle, uDesc, uDate, uId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_event, container, false);

        mTitle = view.findViewById(R.id.edit_title);
        mDesc = view.findViewById(R.id.edit_desc);
        mDate = view.findViewById(R.id.edit_date);
        mSaveBtn = view.findViewById(R.id.save_btn);
        mShowBtn = view.findViewById(R.id.show_btn);
        db = FirebaseFirestore.getInstance();

        Bundle bundle = getArguments();
        if (bundle != null) {
            mSaveBtn.setText("Update");
            uTitle = bundle.getString("uTitle");
            uDesc = bundle.getString("uDesc");
            uDate = bundle.getString("uDate");
            uId = bundle.getString("uId");
            mTitle.setText(uTitle);
            mDesc.setText(uDesc);
            mDate.setText(uDate);
        } else {
            mSaveBtn.setText("Save");
        }

        mShowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to EventFragment or Activity
                // Example for Fragment navigation
                getParentFragmentManager().popBackStack();
            }
        });

        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = mTitle.getText().toString();
                String desc = mDesc.getText().toString();
                String date = mDate.getText().toString();

                if (bundle != null) {
                    String id = uId;
                    updateToFirestore(id, title, desc, date);
                } else {
                    String id = UUID.randomUUID().toString();
                    saveToFirestore(id, title, desc, date);
                }
            }
        });

        return view;
    }

    private void updateToFirestore(String id, String title, String desc, String date) {
        db.collection("Event").document(id).update("title", title, "Desc", desc, "date", date)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(requireContext(), "Data Updated", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(requireContext(), "Error :" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveToFirestore(String id, String title, String desc, String date) {
        if (!title.isEmpty() && !desc.isEmpty() && !date.isEmpty()) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", id);
            map.put("title", title);
            map.put("desc", desc);
            map.put("date", date);

            db.collection("Event").document(id).set(map)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(requireContext(), "Data Saved", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show();
                        }
                    });

        } else
            Toast.makeText(requireContext(), "Empty Fields not Allowed", Toast.LENGTH_SHORT).show();
    }
}
