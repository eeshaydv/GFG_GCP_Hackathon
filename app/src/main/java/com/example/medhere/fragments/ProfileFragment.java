package com.example.medhere.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.medhere.R;
import com.example.medhere.activities.ProfileDetailsActivity;
import com.example.medhere.authentication.LoginActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    private ImageView profileImage;
    private TextView name, number, email, bloodGroup, gender, age;
    private Button editButton;
    private TextDrawable mDrawableBuilder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_profile, null);
        profileImage = v.findViewById(R.id.profileImage);
        name = v.findViewById(R.id.name);
        number = v.findViewById(R.id.number);
        email = v.findViewById(R.id.email);
        bloodGroup = v.findViewById(R.id.bloodgroup);
        gender = v.findViewById(R.id.gender);
        age = v.findViewById(R.id.age);
        editButton = v.findViewById(R.id.edit_details);

        DatabaseReference userRef = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(FirebaseAuth.getInstance().getUid());

        userRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String uName = String.valueOf(snapshot.child("name").getValue().toString());
                    name.setText(uName);
                    number.setText(String.valueOf(snapshot.child("number").getValue().toString()));
                    email.setText(String.valueOf(snapshot.child("email").getValue().toString()));
                    bloodGroup.setText(String.valueOf(snapshot.child("bloodGroup").getValue().toString()));
                    gender.setText(String.valueOf(snapshot.child("gender").getValue().toString()));
                    age.setText(String.valueOf(snapshot.child("age").getValue().toString()));
                    mDrawableBuilder = TextDrawable.builder().buildRound(String.valueOf(uName.charAt(0)), R.color.colorAccent);
                    profileImage.setImageDrawable(mDrawableBuilder);
                }
            }

            @Override
            public void onCancelled(DatabaseError e) {
                Snackbar snackbar = Snackbar.make(v.findViewById(android.R.id.content), "error: " + e.getMessage(), Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(ContextCompat.getColor(container.getContext(), R.color.red));
                snackbar.setTextColor(ContextCompat.getColor(container.getContext(),R.color.white));
                snackbar.show();
            }
        });


        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(container.getContext(), ProfileDetailsActivity.class));
            }
        });
        return v;
    }


}