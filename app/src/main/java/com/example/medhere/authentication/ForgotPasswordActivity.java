package com.example.medhere.authentication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.medhere.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {
    private EditText Email;
    private Button enter;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        Email = findViewById(R.id.password_email);
        enter = findViewById(R.id.password_button);
        mAuth = FirebaseAuth.getInstance();


        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = Email.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Enter your Email ID", Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(ContextCompat.getColor(ForgotPasswordActivity.this, R.color.red));
                    snackbar.setTextColor(ContextCompat.getColor(ForgotPasswordActivity.this,R.color.white));
                    snackbar.show();
                } else {
                    mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(Task<Void> task) {

                            if (task.isSuccessful()) {
                                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "A Link has been sent to your Email ID to change your password", Snackbar.LENGTH_SHORT);
                                snackbar.setBackgroundTint(ContextCompat.getColor(ForgotPasswordActivity.this, R.color.green));
                                snackbar.setTextColor(ContextCompat.getColor(ForgotPasswordActivity.this,R.color.intro_title_color));
                                snackbar.show();

                                Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);

                            } else {
                                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), task.getException().getMessage(), Snackbar.LENGTH_SHORT);
                                snackbar.setBackgroundTint(ContextCompat.getColor(ForgotPasswordActivity.this, R.color.red));
                                snackbar.setTextColor(ContextCompat.getColor(ForgotPasswordActivity.this,R.color.white));
                                snackbar.show();
                            }

                        }
                    });
                }
            }
        });
    }
}