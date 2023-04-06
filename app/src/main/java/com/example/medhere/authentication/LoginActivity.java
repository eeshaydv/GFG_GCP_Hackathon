package com.example.medhere.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.medhere.R;
import com.example.medhere.activities.MainActivity;
import com.example.medhere.base.BaseActivity;
import com.example.medhere.firebase.login.LoginContract;
import com.example.medhere.firebase.login.LoginPresenter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends BaseActivity implements LoginContract.View {

    private String loginEmail, loginPassword;
    private LoginPresenter mLoginPresenter;
    private FirebaseAuth mAuth;

    EditText lEmail;
    EditText lPassword;
    TextView forgotPassword, signUpText;
    Button loginButton;
    ImageView googleSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginPresenter = new LoginPresenter(this);

        lEmail = findViewById(R.id.email_login);
        lPassword = findViewById(R.id.password_login);
        forgotPassword = findViewById(R.id.forgot_password);
        signUpText = findViewById(R.id.register_text);
        loginButton = findViewById(R.id.login_button);
        googleSignIn = findViewById(R.id.google);

        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                finish();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginEmail = lEmail.getText().toString().trim();

                loginPassword = lPassword.getText().toString().trim();

                showLoadingScreen();

                initLogin(loginEmail, loginPassword);
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
                finish();
            }
        });

    }
    private void initLogin(String email, String password) {
        mLoginPresenter.checkCredentials(LoginActivity.this, loginEmail, loginPassword);
    }


    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onLoginSuccess(String message) {
        hideLoadingScreen();
        IsEmailVerified();
    }

    @Override
    public void onLoginFailure(String message) {
        hideLoadingScreen();
        onError(message);
    }

    private void IsEmailVerified() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser Muser = mAuth.getCurrentUser();
        assert Muser != null;
        if (Muser.isEmailVerified()) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        } else {
            startActivity(new Intent(LoginActivity.this, VerificationActivity.class));
            finish();
        }
    }
}