package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class noteDetails extends AppCompatActivity {
    private TextView mDisplayTitle, mDisplayContent;
    private FloatingActionButton mGoToEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);
        getSupportActionBar().hide();
        mDisplayTitle=findViewById(R.id.displayTitleOfNote);
        mDisplayContent=findViewById(R.id.displayContentOfNote);
        mGoToEdit=findViewById(R.id.goToEditNote);
        Intent data=getIntent();
        mGoToEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),editNotesActivity.class);
                intent.putExtra("title",data.getStringExtra("title"));
                intent.putExtra("content", data.getStringExtra("content"));
                intent.putExtra("noteId", data.getStringExtra("noteId"));
                finish();
                view.getContext().startActivity(intent);
            }
        });
        mDisplayTitle.setText(data.getStringExtra("title"));
        mDisplayContent.setText(data.getStringExtra("content"));
    }
}