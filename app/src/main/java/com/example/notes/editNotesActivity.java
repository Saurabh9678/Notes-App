package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

public class editNotesActivity extends AppCompatActivity {
    private EditText mUpdateTitle, mUpdateContent;
    private FloatingActionButton mUpdate;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    ProgressBar mProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_notes);
        getSupportActionBar().hide();
        mUpdate=findViewById(R.id.updateNote);
        mUpdateContent=findViewById(R.id.updateContentOfNote);
        mUpdateTitle=findViewById(R.id.updateTitleOfNote);
        mProgressBar=findViewById(R.id.progressBarOfEditActivity);
        Intent data = getIntent();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        mUpdateTitle.setText(data.getStringExtra("title"));
        mUpdateContent.setText(data.getStringExtra("content"));
        mUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newTitle = mUpdateTitle.getText().toString();
                String newContent = mUpdateContent.getText().toString();
                if (newContent.isEmpty()){
                    Toast.makeText(editNotesActivity.this, "Content is Empty. Better Delete The Note", Toast.LENGTH_SHORT).show();
                }
                else{
                    mProgressBar.setVisibility(View.VISIBLE);
                    DocumentReference documentReference =firebaseFirestore.collection("Notes").document(firebaseUser.getUid()).collection("MyNotes").document(data.getStringExtra("noteId"));
                    Map<String,Object> note = new HashMap<>();
                    note.put("title",newTitle);
                    note.put("content",newContent);
                    documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(editNotesActivity.this,"Note Updated successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(editNotesActivity.this,notesActivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mProgressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(editNotesActivity.this, "Failure to update note, Please Try again", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}