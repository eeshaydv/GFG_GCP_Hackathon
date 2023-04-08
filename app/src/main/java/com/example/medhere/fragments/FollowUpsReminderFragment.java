package com.example.medhere.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.medhere.R;
import com.example.medhere.utils.ViewAnimation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;

public class FollowUpsReminderFragment extends Fragment {

    private FloatingActionButton addReminder, startSession, addFab;
    private boolean isRotate = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_follow_ups_reminder, null);
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
}