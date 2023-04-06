package com.example.medhere.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.medhere.R;
import com.example.medhere.base.BaseActivity;
import com.example.medhere.firebase.register.RegistrationContract;
import com.example.medhere.firebase.register.RegistrationPresenter;

public class SignUpActivity extends BaseActivity implements RegistrationContract.View {

    private RegistrationPresenter mRegistrationPresenter;
    private String Uname, Uemail, Upassword, UconfirmPassword;

    EditText name, email, password, confirmPassword;
    Button signUpButton;
    TextView loginText;
    ImageView googleSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mRegistrationPresenter = new RegistrationPresenter(this);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirm_password);
        signUpButton = findViewById(R.id.register_button);
        loginText = findViewById(R.id.login_text);
        googleSignIn = findViewById(R.id.google);

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                finish();
            }
        });

        signUpButton.setOnClickListener(view -> {
            Uname = name.getText().toString().trim();

            Uemail = email.getText().toString().trim();

            Upassword = password.getText().toString().trim();

            UconfirmPassword = confirmPassword.getText().toString().trim();

            showLoadingScreen();
            mRegistrationPresenter.register(this, Uemail, Upassword, UconfirmPassword, Uname);
        });
    }


    @Override
    public void onRegistrationSuccess(String message) {
        hideLoadingScreen();
        startActivity(new Intent(SignUpActivity.this, VerificationActivity.class));
        finish();
    }

    @Override
    public void onRegistrationFailure(String message) {
        hideLoadingScreen();
        onError(message);
    }

    @Override
    public void onBackPressed() {
        showLoadingScreen();
        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
        finish();
    }
}