package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgotPassword extends AppCompatActivity {
    private EditText mforgotPasswordEmail;
    private RelativeLayout mbtnRecovery;
    private TextView mgoBackLogin;
   private FirebaseAuth firebaseAuth;
   ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        mforgotPasswordEmail=findViewById(R.id.forgotPasswordEmail);
        mbtnRecovery = findViewById(R.id.btnRecovery);
        mgoBackLogin = findViewById(R.id.goBackLogin);
        progressBar=findViewById(R.id.progressBarOfForgotPasswordActivity);
        firebaseAuth= FirebaseAuth.getInstance();
        getSupportActionBar().hide();
        mgoBackLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(forgotPassword.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        mbtnRecovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = mforgotPasswordEmail.getText().toString().trim();
                if (mail.isEmpty()){
                    Toast.makeText(forgotPassword.this, "Enter Your email First", Toast.LENGTH_SHORT).show();
                }else{
                    // we have to send  password recovery mail
                    progressBar.setVisibility(View.VISIBLE);
                    firebaseAuth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(forgotPassword.this, "Mail is sent, You can recover your password now", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(forgotPassword.this, MainActivity.class));
                            }else
                            {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(forgotPassword.this, "Account not exist", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}