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

public class signUp extends AppCompatActivity {
    private EditText msignupEmail, msignupPassword;
    private RelativeLayout msignup;
    private TextView mgoToLogin;
    private FirebaseAuth firebaseAuth;
    ProgressBar mProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getSupportActionBar().hide();
        msignupEmail=findViewById(R.id.signUpEmail);
        msignupPassword=findViewById(R.id.signUpPassword);
        msignup=findViewById(R.id.signUp);
        mgoToLogin=findViewById(R.id.goToLogin);
        mProgressBar=findViewById(R.id.progressBarOfSignupActivity);
        firebaseAuth=FirebaseAuth.getInstance();
        mgoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(signUp.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        msignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = msignupEmail.getText().toString().trim();
                String password = msignupPassword.getText().toString().trim();
                if (mail.isEmpty() || password.isEmpty()){
                    Toast.makeText(signUp.this, "All Fields Must Be filled", Toast.LENGTH_SHORT).show();
                }else if(password.length()<7){
                    Toast.makeText(signUp.this, "Password Length must be more than 7", Toast.LENGTH_SHORT).show();
                }else{
                    //register to firebase
                    mProgressBar.setVisibility(View.VISIBLE);
                    firebaseAuth.createUserWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(signUp.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                sendEmailVerification();
                            }else{
                                mProgressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(signUp.this, "Failed to register", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    // Send email verification
    private void sendEmailVerification(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser!= null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(signUp.this, "Verification mail is sent, Please verify the mail and login again", Toast.LENGTH_SHORT).show();
                    firebaseAuth.signOut();
                    finish();
                    startActivity(new Intent(signUp.this,MainActivity.class));
                }
            });
        }else{
            mProgressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "Failed to send verification mail", Toast.LENGTH_SHORT).show();
        }
    }
}