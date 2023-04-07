package com.example.medhere.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.medhere.R;
import com.example.medhere.authentication.LoginActivity;
import com.example.medhere.onBoardingActivities.SplashScreen;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileDetailsActivity extends AppCompatActivity {

    private CircleImageView profilePicture;
    private EditText name, number, email, bloodGroup, gender, age;
    private Button saveDetails;
    private String uName, uNumber, uEmail, uBloodGroup, uGender, uAge;

    private DatabaseReference rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (restorePrefData()) {

            Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class );
            startActivity(mainActivity);
            finish();
        }
        setContentView(R.layout.activity_profile_details);

        name = findViewById(R.id.name);
        number = findViewById(R.id.number);
        email = findViewById(R.id.email);
        bloodGroup = findViewById(R.id.bloodgroup);
        gender = findViewById(R.id.gender);
        age = findViewById(R.id.age);
        saveDetails = findViewById(R.id.submit_details);

        rootRef = FirebaseDatabase.getInstance().getReference("users");

        saveDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uName = name.getText().toString().trim();
                uNumber = number.getText().toString().trim();
                uEmail = email.getText().toString().trim();
                uBloodGroup = bloodGroup.getText().toString().trim();
                uGender = gender.getText().toString().trim();
                uAge = age.getText().toString().trim();

                validateDetails(uName, uNumber, uEmail, uBloodGroup, uGender, uAge);
                savePrefsData();
            }
        });
    }

    private void validateDetails(String uName, String uNumber, String uEmail, String uBloodGroup, String uGender, String uAge) {
        if(uName.isEmpty()){
            name.setError("Required");
            showSnackBar("Name");
            return;
        }

        if(uNumber.isEmpty()){
            number.setError("Required");
            showSnackBar("Phone Number");
            return;
        }

        if(uEmail.isEmpty()){
            email.setError("Required");
            showSnackBar("Email ID");
            return;
        }

        if(uBloodGroup.isEmpty()){
            bloodGroup.setError("Required");
            showSnackBar("Blood Group");
            return;
        }

        if(uGender.isEmpty()){
            gender.setError("Required");
            showSnackBar("Gender");
            return;
        }

        if(uAge.isEmpty()){
            age.setError("Required");
            showSnackBar("Age");
            return;
        }

        storeDetailsInDb(uName, uNumber, uEmail, uBloodGroup, uGender, uAge);

    }

    private void storeDetailsInDb(String uName, String uNumber, String uEmail, String uBloodGroup, String uGender, String uAge) {

        Map detailsMap = new HashMap();
        detailsMap.put("name", uName);
        detailsMap.put("number", uNumber);
        detailsMap.put("email", uEmail);
        detailsMap.put("bloodGroup", uBloodGroup);
        detailsMap.put("gender", uGender);
        detailsMap.put("age", uAge);

        String currUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        rootRef.child(currUid).setValue(detailsMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Details Stored !", Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(ContextCompat.getColor(ProfileDetailsActivity.this, R.color.green));
                        snackbar.setTextColor(ContextCompat.getColor(ProfileDetailsActivity.this,R.color.white));
                        snackbar.show();

                        savePrefsData();

                        Intent intent=new Intent(ProfileDetailsActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "error: " + e.getMessage(), Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(ContextCompat.getColor(ProfileDetailsActivity.this, R.color.red));
                        snackbar.setTextColor(ContextCompat.getColor(ProfileDetailsActivity.this,R.color.white));
                        snackbar.show();
                    }
                });


    }

    private void showSnackBar(String uName) {
        Snackbar snackbar1 = Snackbar.make(findViewById(android.R.id.content), "Please Enter your "+ uName + " !", Snackbar.LENGTH_LONG);
        snackbar1.setBackgroundTint(ContextCompat.getColor(this, R.color.yellow));
        snackbar1.setTextColor(ContextCompat.getColor(this,R.color.intro_title_color));
        snackbar1.show();
    }


    private boolean restorePrefData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        Boolean isIntroActivityOpnendBefore = pref.getBoolean("isProfileOpnend",false);
        return  isIntroActivityOpnendBefore;
    }

    private void savePrefsData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isProfileOpnend",true);
        editor.commit();
    }

}