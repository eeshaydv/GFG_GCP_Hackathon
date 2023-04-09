package com.example.medhere.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.medhere.R;
import com.example.medhere.adapter.MedicineReminderFragmentAdapter;
import com.example.medhere.models.MedicineReminder;
import com.example.medhere.utils.ViewAnimation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;

public class MedicineRemainderFragment extends Fragment {

    private FloatingActionButton addReminder, startSession, addFab;
    private RecyclerView medicinesReminderList;
    private MedicineReminderFragmentAdapter adapter;
    private boolean isRotate = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_medicine_remainder, null);

        medicinesReminderList = v.findViewById(R.id.calls_list);
        medicinesReminderList.setLayoutManager(new LinearLayoutManager(getContext()));
        medicinesReminderList.setHasFixedSize(true);
        adapter = new MedicineReminderFragmentAdapter(getActivity(), init());
        medicinesReminderList.setAdapter(adapter);
        addReminder = v.findViewById(R.id.add_fab);
        startSession = v.findViewById(R.id.new_session);
        addFab = v.findViewById(R.id.calls_fab);
        ViewAnimation.init(addReminder);
        ViewAnimation.init(startSession);

        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRotate = ViewAnimation.rotateFab(v, !isRotate);
                if (isRotate) {
                    ViewAnimation.showIn(addReminder);
                    ViewAnimation.showIn(startSession);
                } else {
                    ViewAnimation.showOut(addReminder);
                    ViewAnimation.showOut(startSession);
                }
            }
        });

        return v;
    }

    private ArrayList<MedicineReminder> init() {
        ArrayList<MedicineReminder> list = new ArrayList<>();

        list.add(new MedicineReminder("TEARS NATURALE FORTE EYE DROPS", "2 Drops", "8 : 00 AM"));
        list.add(new MedicineReminder("LACRIGEL EYE OINTMENT", "Sufficient Quantity", "8 : 15 AM"));
        list.add(new MedicineReminder("FOLITRAX 15 MG", "One Tablet", "8 : 00 AM"));
        list.add(new MedicineReminder("FOLIC ACID 5 MG", "One Tablet", "10 : 00 PM"));
        list.add(new MedicineReminder("HCQ 200 MG", "One Tablet", "10 : 00 PM"));

        return list;
    }
}