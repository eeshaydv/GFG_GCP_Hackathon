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
import com.example.medhere.activities.ProfileDetailsActivity;
import com.example.medhere.base.BaseActivity;
import com.example.medhere.firebase.register.RegistrationContract;
import com.example.medhere.firebase.register.RegistrationPresenter;
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

public class SignUpActivity extends BaseActivity implements RegistrationContract.View {

    static final int GOOGLE_SIGN=123;

    private RegistrationPresenter mRegistrationPresenter;
    private String Uname, Uemail, Upassword, UconfirmPassword;
    private FirebaseAuth mAuth;

    EditText name, email, password, confirmPassword;
    Button signUpButton;
    TextView loginText;
    ImageView googleSignIn;

    GoogleSignInClient mGooglesignInClient;

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
        mAuth=FirebaseAuth.getInstance();

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
    }

    void signinGoogle(){
        showLoadingScreen();
        Intent signInIntent=mGooglesignInClient.getSignInIntent();
        startActivityForResult(signInIntent,GOOGLE_SIGN);
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
                    Intent intent=new Intent(SignUpActivity.this, ProfileDetailsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    FirebaseUser user =mAuth.getCurrentUser();
                }else{
                    hideLoadingScreen();
                    Toast.makeText(SignUpActivity.this,"SignIn Successful",Toast.LENGTH_LONG).show();
                }
            }
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