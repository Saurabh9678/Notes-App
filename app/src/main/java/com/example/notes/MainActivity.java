package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private EditText mloginEmail, mloginPassword;
    private RelativeLayout mgoToSignup, mLogin;
    private TextView mForgotPassword;
    private FirebaseAuth firebaseAuth;
    private ProgressBar mprogressbarOfMain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        mprogressbarOfMain = findViewById(R.id.progressBarOfMainActivity);
        mloginEmail=findViewById(R.id.loginEmail);
        mloginPassword=findViewById(R.id.loginPassword);
        mgoToSignup=findViewById(R.id.goToSignUp);
        mLogin=findViewById(R.id.login);
        mForgotPassword=findViewById(R.id.forgotPassword);
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        if (firebaseUser!=null){
            finish();
            startActivity(new Intent(MainActivity.this, notesActivity.class));
        }

        mgoToSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(MainActivity.this, signUp.class);
                startActivity(intent);
                finish();
            }
        });
        mForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, forgotPassword.class);
                startActivity(intent);
                finish();
            }
        });
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = mloginEmail.getText().toString().trim();
                String password = mloginPassword.getText().toString().trim();
                if (mail.isEmpty() || password.isEmpty()){
                    Toast.makeText(MainActivity.this, "Enter all details", Toast.LENGTH_SHORT).show();
                }else{
                    //login user
                        mprogressbarOfMain.setVisibility(View.VISIBLE);
                    firebaseAuth.signInWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    checkMailVerification();
                                }    else{
                                    Toast.makeText(MainActivity.this, "Account Doesn't Exist", Toast.LENGTH_SHORT).show();
                                    mprogressbarOfMain.setVisibility(View.INVISIBLE);
                                }
                        }
                    });

                }
            }
        });
    }
    private void checkMailVerification(){
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        assert firebaseUser != null;
        if (firebaseUser.isEmailVerified()) {
            Toast.makeText(this, "You are logged in", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(MainActivity.this, notesActivity.class));
        } else {
            mprogressbarOfMain.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "Verify your mail first", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }
}