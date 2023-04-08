package com.example.medhere.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medhere.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;

import java.util.Calendar;

public class HealthNotesFragment extends Fragment  {

    private DatabaseReference rootRef;

    private TextView date, day, notes;
    private EditText txt;
    private String prevNotes = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_health_notes, null);

        date = v.findViewById(R.id.date);
        notes = v.findViewById(R.id.notes);
        rootRef = FirebaseDatabase.getInstance().getReference("notes");
        DatePicker datePicker = (DatePicker) v.findViewById(R.id.calender);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {
                //Toast.makeText(container.getContext(), "Date= "+ "Year=" + year + " Month=" + (month + 1) + " day=" + dayOfMonth, Toast.LENGTH_LONG).show();
                AlertDialog.Builder alertName = new AlertDialog.Builder(getActivity());
                final EditText editTextName1 = new EditText(getActivity());

                String cDate = ""+year+"-"+month+"-"+dayOfMonth;
                alertName.setTitle("Enter Notes for Date : "+ cDate);
                alertName.setView(editTextName1);
                LinearLayout layoutName = new LinearLayout(getActivity());
                layoutName.setOrientation(LinearLayout.VERTICAL);
                layoutName.addView(editTextName1); // displays the user input bar
                alertName.setView(layoutName);

                alertName.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        txt = editTextName1; // variable to collect user input
                        collectInput(year, month, dayOfMonth); // analyze input (txt) in this method
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        date.setText(cDate);
                        String currUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        rootRef.child(currUid).child(cDate).addListenerForSingleValueEvent(new ValueEventListener() {
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

                        if(!prevNotes.isEmpty()){
                            notes.setText(prevNotes);
                            prevNotes = "";
                        }

                        dialog.cancel(); // closes dialog
                    }
                });

                alertName.show();
            }
        });

        return v;
    }

    public void collectInput(int year, int month, int dayOfMonth){
        // convert edit text to string
        String getInput = txt.getText().toString();


        // ensure that user input bar is not empty
        if (getInput ==null || getInput.trim().equals("")){
            Toast.makeText(getActivity(), "Please add some Notes", Toast.LENGTH_LONG).show();
        }
        // add input into an data collection arraylist
        else {

            String cDate = ""+year+"-"+month+"-"+dayOfMonth;
            String currUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            rootRef.child(currUid).child(cDate).addListenerForSingleValueEvent(new ValueEventListener() {
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
            rootRef.child(currUid).child(cDate).setValue(getInput)
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

            date.setText(cDate);
            notes.setText(getInput);
            prevNotes = "";
        }
    }
}