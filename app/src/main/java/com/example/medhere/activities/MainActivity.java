package com.example.medhere.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.medhere.R;
import com.example.medhere.authentication.LoginActivity;
import com.example.medhere.authentication.SignUpActivity;
import com.example.medhere.base.BaseActivity;
import com.example.medhere.fragments.FollowUpsReminderFragment;
import com.example.medhere.fragments.HealthNotesFragment;
import com.example.medhere.fragments.HealthVitalsFragment;
import com.example.medhere.fragments.MedicineRemainderFragment;
import com.example.medhere.fragments.PastRecordsFragment;
import com.example.medhere.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends BaseActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener{

    ImageView thumbnail, notifications, settings;
    private TextDrawable mDrawableBuilder;
    private FirebaseAuth mAuth;
    private String currentUserID;
    private DatabaseReference userRef;
    private DatabaseReference RootRef;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!haveNetworkConnection())
        {
            Snackbar snackbar1 = Snackbar.make(findViewById(android.R.id.content), "Check your Internet Connection, You need Stable Internet Connection to use this App", Snackbar.LENGTH_LONG);
            snackbar1.setBackgroundTint(ContextCompat.getColor(this, R.color.yellow));
            snackbar1.setTextColor(ContextCompat.getColor(this,R.color.intro_title_color));
            snackbar1.show();
        }

        if (!restorePrefData()) {

            Intent mainActivity = new Intent(getApplicationContext(), ProfileDetailsActivity.class );
            startActivity(mainActivity);
            finish();
        }

        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        loadFragment(new MedicineRemainderFragment());
        navigation.setSelectedItemId(R.id.nav_Medicines);

        thumbnail = findViewById(R.id.thumbnail);
        notifications = findViewById(R.id.notifications);
        settings = findViewById(R.id.settings);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        currentUserID = mAuth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance().getReference();
        userRef = FirebaseDatabase.getInstance().getReference();

        thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new ProfileFragment());
            }
        });



        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            }
        });

    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment = null;


        switch (item.getItemId()) {
            case R.id.nav_notes:
                fragment = new HealthNotesFragment();
                break;

            case R.id.nav_documents:
                fragment = new PastRecordsFragment();
                break;

            case R.id.nav_Medicines:
                fragment = new MedicineRemainderFragment();
                break;

            case R.id.nav_follow_ups:
                fragment = new FollowUpsReminderFragment();
                break;

            case R.id.nav_vitals:
                fragment = new HealthVitalsFragment();
                break;

        }

        if (fragment == null) fragment = new MedicineRemainderFragment();

        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
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