package com.example.medhere.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medhere.R;
import com.example.medhere.interfaces.OnDateSelectedListener;
import com.example.medhere.models.CalendarDate;
import com.example.medhere.views.CustomCalendarView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;

public class HealthNotesFragment extends Fragment implements OnDateSelectedListener {

    private DatabaseReference rootRef;

    private CustomCalendarView mCustomCalendar;
    private TextView date, day, notes;
    private ArrayList<CharSequence> arrayListCollection = new ArrayList<>();
    private ArrayAdapter<CharSequence> adapter;
    private EditText txt;
    private String prevNotes = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_health_notes, null);

        date = v.findViewById(R.id.date);
        day = v.findViewById(R.id.day);
        notes = v.findViewById(R.id.notes);

        rootRef = FirebaseDatabase.getInstance().getReference("notes");

        return v;
    }

    @Override
    public void onDateSelected(CalendarDate calDate) {
        AlertDialog.Builder alertName = new AlertDialog.Builder(getActivity());
        final EditText editTextName1 = new EditText(getActivity());

        alertName.setTitle("Enter Notes for Date : "+ calDate.dayToString());
        alertName.setView(editTextName1);
        LinearLayout layoutName = new LinearLayout(getActivity());
        layoutName.setOrientation(LinearLayout.VERTICAL);
        layoutName.addView(editTextName1); // displays the user input bar
        alertName.setView(layoutName);

        alertName.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                txt = editTextName1; // variable to collect user input
                collectInput(calDate); // analyze input (txt) in this method
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel(); // closes dialog
                alertName.show();
            }
        });
    }

    public void collectInput(CalendarDate calDate){
        // convert edit text to string
        String getInput = txt.getText().toString();


        // ensure that user input bar is not empty
        if (getInput ==null || getInput.trim().equals("")){
            Toast.makeText(getActivity(), "Please add some Notes", Toast.LENGTH_LONG).show();
        }
        // add input into an data collection arraylist
        else {

            String currUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            rootRef.child(currUid).child(calDate.dayToString()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        prevNotes = snapshot.getValue().toString();
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {

                }
            });

            getInput = prevNotes + " " + getInput;
            rootRef.child(currUid).child(calDate.dayToString()).setValue(getInput)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getActivity(), "Notes Added Successfully!", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            Toast.makeText(getActivity(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
                        }
                    });

            date.setText(calDate.dayToString());
            day.setText(calDate.dayOfWeekToStringName());
            notes.setText(getInput);
        }
    }
}
