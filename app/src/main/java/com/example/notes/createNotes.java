package com.example.notes;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class createNotes extends AppCompatActivity {
    private EditText mTitle, mContent;
    private FloatingActionButton mSaveBtn;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;
    private ProgressBar mProgressBarOfCreateNote;
    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_notes);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getSupportActionBar().hide();
        mProgressBarOfCreateNote=findViewById(R.id.progressBarOfCreateActivity);
        mTitle = findViewById(R.id.createTitleOfNote);
        mContent = findViewById(R.id.createContentOfNote);
        mSaveBtn = findViewById(R.id.saveNote);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore =FirebaseFirestore.getInstance();
        firebaseUser =firebaseAuth.getCurrentUser();

        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = mTitle.getText().toString();
                String content = mContent.getText().toString();
                if (content.isEmpty()){
                    Toast.makeText(createNotes.this, "Body is empty", Toast.LENGTH_SHORT).show();
                }else{
                        mProgressBarOfCreateNote.setVisibility(View.VISIBLE);
                    DocumentReference documentReference = firebaseFirestore.collection("Notes").document(firebaseUser.getUid()).collection("MyNotes").document();
                    Map<String, Object> note = new HashMap<>();
                    note.put("title", title);
                    note.put("content",content);
                    documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(createNotes.this,"Note Created successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(createNotes.this,notesActivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mProgressBarOfCreateNote.setVisibility(View.INVISIBLE);
                            Toast.makeText(createNotes.this, "Failure to create note, Please Try again", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}