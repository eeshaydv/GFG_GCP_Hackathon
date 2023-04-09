package com.example.medhere.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.medhere.R;
import com.example.medhere.adapter.FollowUpReminderAdapter;
import com.example.medhere.adapter.FollowUpReminderFragmentAdapter;
import com.example.medhere.adapter.MedicineReminderFragmentAdapter;
import com.example.medhere.models.FollowUpReminder;
import com.example.medhere.models.MedicineReminder;
import com.example.medhere.utils.ViewAnimation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;

public class FollowUpsReminderFragment extends Fragment {

    private FloatingActionButton addReminder, startSession, addFab;
    private RecyclerView followUpsList;
    private FollowUpReminderFragmentAdapter adapter;
    private boolean isRotate = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_follow_ups_reminder, null);

        followUpsList = v.findViewById(R.id.list);
        followUpsList.setLayoutManager(new LinearLayoutManager(getContext()));
        followUpsList.setHasFixedSize(true);
        adapter = new FollowUpReminderFragmentAdapter(getActivity(), init());
        followUpsList.setAdapter(adapter);
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

    private ArrayList<FollowUpReminder> init() {
        ArrayList<FollowUpReminder> list = new ArrayList<>();

        list.add(new FollowUpReminder("Doctor 1", "3-4-23", "8 : 00 AM"));
        list.add(new FollowUpReminder("Doctor 2", "5-6-22", "10 : 15 AM"));
        list.add(new FollowUpReminder("Doctor 3", "7-4-23", "9 : 00 AM"));
        list.add(new FollowUpReminder("Doctor 5", "16-7-21", "10 : 00 PM"));
        list.add(new FollowUpReminder("Doctor 4", "1-1-23", "10 : 00 PM"));

        return list;
    }
}