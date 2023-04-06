package com.example.medhere.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medhere.R;
import com.example.medhere.activities.MainActivity;
import com.example.medhere.base.BaseActivity;
import com.example.medhere.firebase.login.LoginContract;
import com.example.medhere.firebase.login.LoginPresenter;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.annotations.Nullable;

public class LoginActivity extends BaseActivity implements LoginContract.View {

    static final int GOOGLE_SIGN=123;

    private String loginEmail, loginPassword;
    private LoginPresenter mLoginPresenter;
    private FirebaseAuth mAuth;

    EditText lEmail;
    EditText lPassword;
    TextView forgotPassword, signUpText;
    Button loginButton;
    ImageView googleSignIn;

    GoogleSignInClient mGooglesignInClient;

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
        mAuth=FirebaseAuth.getInstance();

        GoogleSignInOptions googleSignInOptions=new GoogleSignInOptions.Builder()
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGooglesignInClient= GoogleSignIn.getClient(this,googleSignInOptions);

        googleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signinGoogle();
            }
        });

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

    void signinGoogle(){
       showLoadingScreen();
        Intent signInIntent=mGooglesignInClient.getSignInIntent();
        startActivityForResult(signInIntent,GOOGLE_SIGN);
    }
    private void initLogin(String email, String password) {
        mLoginPresenter.checkCredentials(LoginActivity.this, loginEmail, loginPassword);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GOOGLE_SIGN){
            Task<GoogleSignInAccount> task=GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                GoogleSignInAccount account=task.getResult(ApiException.class);
                if(account!=null){
                    firebaseAuthWithGoogleAccount(account);
                }
            }catch (ApiException e){
                e.printStackTrace();
            }
        }
    }

    private void firebaseAuthWithGoogleAccount(GoogleSignInAccount account) {
        AuthCredential credential= GoogleAuthProvider.getCredential(account.getIdToken(),null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {
                if(task.isSuccessful()){
                    hideLoadingScreen();
                    Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    FirebaseUser user =mAuth.getCurrentUser();
                }else{
                    hideLoadingScreen();
                    Toast.makeText(LoginActivity.this,"SignIn Successful",Toast.LENGTH_LONG).show();
                }
            }
        });


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