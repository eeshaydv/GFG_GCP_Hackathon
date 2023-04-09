package com.example.medhere.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.medhere.R;
import com.example.medhere.adapter.FollowUpReminderFragmentAdapter;
import com.example.medhere.adapter.PastRecordsAdapter;
import com.example.medhere.models.FollowUpReminder;
import com.example.medhere.models.PastRecordItem;
import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;

public class PastRecordsFragment extends Fragment {

    RecyclerView pastRecordsList;
    PastRecordsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_past_records, null);

        pastRecordsList = v.findViewById(R.id.list);
        pastRecordsList.setLayoutManager(new LinearLayoutManager(getContext()));
        pastRecordsList.setHasFixedSize(true);
        adapter = new PastRecordsAdapter(getActivity(), init());
        pastRecordsList.setAdapter(adapter);

        return v;
    }

    private ArrayList<PastRecordItem> init() {
        ArrayList<PastRecordItem> list = new ArrayList<>();

        list.add(new PastRecordItem("Doctor 1", "3-4-23"));
        list.add(new PastRecordItem("Doctor 2", "5-6-22"));
        list.add(new PastRecordItem("Doctor 3", "7-4-23"));
        list.add(new PastRecordItem("Doctor 5", "16-7-21"));
        list.add(new PastRecordItem("Doctor 4", "1-1-23"));

        return list;
    }
}